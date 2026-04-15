// 请求接口
export const BASE_URL = 'http://apitest.yuelaobaobao.com/api/app'; //正式接口

export const AMAPKEY = 'afc1108338a00b4719cd5922e98bcd8a'; //高德定位小程序

export const myRequest = (options) => {
	if (options && options.withToken) {
		options.data = {
			...options.data
		};
	}
	if(options.withLoading){
		uni.showLoading({
			title: '加载中'
		});
	}
	
	return new Promise((resolve, reject) => {
		uni.request({
			url: BASE_URL + "/"+options.url,
			method: options.method || "GET",
			data: options.data || {},
			header: {
				Authorization: options.withToken? uni.getStorageSync("token"):'',
			},
			success: (res) => {
				if (res.data.code == 401 || res.data.code == 2) {
					uni.showToast({
						icon: 'none',
						title: '登录失效，请重新登录',
						duration: 2000
					})
					uni.removeStorageSync('token');
					uni.removeStorageSync('info');
					uni.removeStorageSync('itemobj');
					setTimeout(() => {
						uni.reLaunch({
							url: '/pageslogin/index/index'
						});
					}, 200);
				}
				resolve(res);
			},
			fail: (err) => {
				uni.showToast({
					title: "请求接口失败",
				});
				reject(err);
			},
			complete() {
				uni.hideLoading();
			}
		});
	});
};
//登录判断
export const getId = () => {
	return new Promise((resolve, reject) => {
		uni.getStorage({
			key: 'info',
			success: (res) => {
				console.log(res, "登录成功");
				resolve(200);
			},
			fail: (err) => {
				console.log(err, '登录失败');
				resolve(11003);
			}
		});
	})
}