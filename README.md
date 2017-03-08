# MVP
项目中的MVP架子
	网络请求结合Rxjava + Retrofit + Ohhttp
	整体思路：
	一、抽取顶层的接口Presenter接口,使用泛型进行简化，包含两个主要的方法与生命周期进行绑定
		public interface Presenter<T extends MvpView> {

		    // 在Activity或者Fragment的生命周期onCreate时进行绑定
		    void attachView(T mvpView);

		    // 在Activity或者Fragment的生命周期onDestroy时进行解绑
		    void detachView();

		}
	二、View层的顶层接口MvpView
		public interface MvpView {
			// 这里对错误可以进行统一提示
	    	void showErrorInfo(String msg);

		}
	三、Presenter的基类实现
		public class BasePresenter<T extends MvpView> implements Presenter<T> {
			// 用于网络请求	
		    protected Api mApi = new ApiImpl();

		    protected String TAG = getClass().getSimpleName();
		    // Presenter持有View的引用,在attachView时获得实例,在detachView时置为null
		    private T mMvpView;
		    // 在attach时获得实例
		    @Override
		    public void attachView(T mvpView) {
		        mMvpView = mvpView;
		    }
		    // 在detach时置为null
		    @Override
		    public void detachView() {
		        mMvpView = null;
		    }

		    public T getMvpView() {
		        return mMvpView;
		    }
		    // 通过java的反射机制获取泛型的实例，简化代码的书写
		    public Class<T> getMvpViewClass() {
		        return ReflexUtil.getClassGeneric(this, 0);
		    }

		}
	四、Api接口，主要是一些基于Rxjava观察者模式的一些网络请求方法
		public interface Api {

		    Subscription getQRcodeInfo(String action, String params, ResultSubscriber<LinkedTreeMap> subscriber);

		    Subscription getNews(int page, int rows, String mid, ResultSubscriber<List<NewEntity>> subscriber);

		    Subscription getCompic(ResultSubscriber<List<CompicEntity>> subscriber);

		    Subscription login(String usercode, String password, ResultSubscriber<Void> subscriber);

		    Subscription getCurruser(ResultSubscriber<UserInfo> subscriber);

		}
	五、Api接口的实现类

		public class ApiImpl implements Api {
			// ApiService的实例，在构造实例时进行初始化
		    private ApiService mApiService;

		    public ApiImpl() {
		        this.mApiService = RetrofitManage.getApiInstance(ApiService.class);
		    }

		    @Override
		    public Subscription getNews(int page, int rows, String mid, ResultSubscriber<List<NewEntity>> subscriber) {
		        return HttpMethod.execute(mApiService.news(page, rows, mid), subscriber);
		    }

		    @Override
		    public Subscription login(String usercode, String password, ResultSubscriber<Void> subscriber) {
		        return HttpMethod.execute(mApiService.login(usercode, password), subscriber);
		    }
		}
	六、ApiService接口，实现具体网络访问，主要是Retrofit的使用注解的get或者post请求

		public interface ApiService {

		    @FormUrlEncoded
		    @POST("{action}")
		    Observable<Response<ResultModle<LinkedTreeMap>>> qrcodeinf(@Path(value = "action", encoded = true) String url, @FieldMap Map<String, String> fields);

		    @GET("index/news!jsonlist.action")
		    Observable<Response<ResultModle<List<NewEntity>>>> news(@Query("page") int page, @Query("rows") int rows, @Query("mid") String mid);

		    @GET("index/news!getByidinfo.action")
		    Observable<Response<ResultModle<NewEntity>>> newsinfo(@Query("id") String id);

		    @GET("common/compic!jsonlist.action")
		    Observable<Response<ResultModle<List<CompicEntity>>>> compic();
		}
	七、请求结果返回模型的统一封装，网络请求返回的json主要字段进行统一管理
		public class ResultModle<T> {

		    private int code = 0;
		    private String msg;
		    private T data;

		    public int getCode() {
		        return code;
		    }

		    public void setCode(int code) {
		        this.code = code;
		    }

		    public String getMsg() {
		        return msg;
		    }

		    public void setMsg(String msg) {
		        this.msg = msg;
		    }

		    public T getData() {
		        return data;
		    }

		    public void setData(T data) {
		        this.data = data;
		    }

		}

	八、Retrofit的网络管理类
		public class RetrofitManage {

		    private static RetrofitManage mRetrofitManage;

		    public static void init(OkHttpClient okHttpClient) {
		        mRetrofitManage = new RetrofitManage(okHttpClient);
		    }

		    private Retrofit mRetrofit;

		    private Map<Class<?>, Object> mApis;
		    // 私有构造方法，标准的单例设计模式
		    private RetrofitManage(OkHttpClient okHttpClient) {

		        mApis = new HashMap<Class<?>, Object>();

		        mRetrofit = new Retrofit.Builder()
		                .client(okHttpClient)
		                .baseUrl(ServiceConfig.BASE_URL)
		                .addConverterFactory(GsonConverterFactory.create())
		                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
		                .build();
		    }

		    /**
		     * 单例模式的Api实例获取
		     * @param clazz 需要获取实例的类
		     * @param <T>   泛型
		     * @return 实例
		     */
		    public static <T> T getApiInstance(Class<T> clazz) {
		        T object = (T) mRetrofitManage.mApis.get(clazz);
		        if (object == null) {
		            object = mRetrofitManage.mRetrofit.create(clazz);
		            mRetrofitManage.mApis.put(clazz, object);
		        }
		        return object;
		    }
		}
	九、ResultSubscriber类，继承自Subscriber，接口数据统一返回类,观察者,持有Presenter层引用
		public abstract class ResultSubscriber<T> extends Subscriber<T> {

		    private BasePresenter mPresenter;

		    public ResultSubscriber(BasePresenter presenter) {
		        mPresenter = presenter;
		    }

		    @Override
		    public void onStart() {
		        super.onStart();
		    }

		    @Override
		    public void onCompleted() {
		    }

		    @Override
		    public void onError(Throwable e) {
		        e.printStackTrace();
		        if (mPresenter != null) {
		            if (mPresenter.getMvpView() != null) {
		                onError(e.getMessage());
		            }
		        } else {
		            onError(e.getMessage());
		        }
		    }

		    /**
		     * 如果有特别错误处理需求.可以重写改方法
		     * @param error
		     */
		    protected void onError(String error) {
		        if (mPresenter != null) {
		            mPresenter.getMvpView().showErrorInfo(error);
		        }
		    }

		    @Override
		    public void onNext(T t) {
		        if (mPresenter != null) {
		            if (mPresenter.getMvpView() != null) {
		                onSuccess(t);
		            }
		        } else {
		            onSuccess(t);
		        }
		        onCompleted();
		    }

		    public abstract void onSuccess(T t);

		}
	

