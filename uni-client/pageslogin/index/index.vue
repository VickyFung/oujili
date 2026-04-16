<template>
	<view class="home-page-container" @touchmove.stop.prevent="() => {}">
		<view class="" v-if="falg == true">
			<image class="bgc-imga" src="../../static/images/bg-c.png" mode="widthFix"></image>
			<image class="bgc-img" src="../static/images/home-bgc.png" mode="aspectFit"></image>
			<image class="logo-img" src="../static/images/logo.png"></image>
			<!-- <view class="start-btn" @click="handleShowTips">
				<view>开始邂逅</view>
			</view> -->
			<button class="start-btn"
				open-type="getPhoneNumber" 
				@getphonenumber="onGetPhoneNumber"
			>
				开始邂逅（手机号快捷登录）
			</button>
			<u-popup u-popup :show="tipsShow" mode="center" round="12" :safeAreaInsetBottom="false">
				<view class="popup-container">
					<!-- <image class="pop-bgc" :src="img + '/img/home-pop-bg.png'"></image> -->
					<view class="div-popo">
						<view class="pop-title">温馨提示</view>
						<view class="tips-content">
							请您仔细阅读并充分理解相关条款， 点击同意即代表您已阅读并同意
							<view class="agreement-btn">《用户协议》</view>
							、
							<view class="agreement-btn">《隐私政策》</view>
						</view>
						<view class="btn-c">
							<view class="pop-btn agree" @click="handAgree">同意</view>
							<view class="pop-btn" @click="handleNoAgree">不同意</view>
						</view>
					</view>
				</view>
			</u-popup>
		</view>
		<ELM ref="elm" :msg="tipMsg" :isConfirm="isConfirm" @confirm="confirm"></ELM>
	</view>
</template>
<script>
	import ELM from '@/components/elm-toast/index.vue';
	export default {
		data() {
			return {
				img: this.$BASE_URL,
				tipsShow: false,
				falg: false,
				num: '',
				tipMsg: '',
				isConfirm: false
			};
		},
		onShow() {
			if (uni.getStorageSync('itemobj')) {
				uni.switchTab({
					url: '/pages/tab/index'
				});
				// this.falg = false
			} else {
				console.log('[]');
				setTimeout(() => {
					this.falg = true;
				}, 1000);
			}
		},
		components: {
			ELM
		},
		methods: {
			async onGetPhoneNumber(e) {
				console.log('获取手机号事件:', e);
				// 1. 用户授权成功
				if (e.detail.errMsg === 'getPhoneNumber:ok') {
					const phoneCode = e.detail.code; // 动态令牌，5分钟有效，仅用一次
					
					// 2. 调用 wx.login 获取登录 code（用于后端换取 session_key/openid）
					const loginRes = await wx.login();
					if (loginRes.code) {
						// 3. 将两个 code 传给后端解密
						this.getPhoneFromServer(phoneCode, loginRes.code);
					}
				} else {
					// 用户拒绝授权或授权失败
					console.log('授权失败:', e.detail.errMsg);
					wx.showToast({ title: '授权失败', icon: 'none' });
				}
			},

			// 请求后端接口
			async getPhoneFromServer(phoneCode, loginCode) {
				const res = await this.$myRequest({
					url: 'auth/login',
					data: {
						loginType: "MINIPROGRAM",
						clientType: "APP",
						wxCode: loginCode,
						phoneCode
					},
					method: 'POST'
				});
				if (res.statusCode !== 200) {
					console.error('网络请求失败:', res);
					wx.showToast({ title: '登录失败', icon: 'none' });
					return;
				}
				if (res.data.code == 200) {
					console.log('后端解密成功:', res.data);
					const { token, userId } = res.data.data;
					uni.setStorageSync('userId', userId);
					uni.setStorageSync('token', token);
					const { loginTIM } = require('@/util/tim.js');
					loginTIM(String(userId)).catch(err => {
						console.error('[TIM] 登录页登录失败', err);
					});
					const resInfo = await this.$myRequest({
						url: 'user/getInfo',
						withToken: true,
						data: {
							id: userId
						},
						method: 'POST'
					});
					if (resInfo.data?.code == 200) {
						const userInfo = resInfo.data.data;
						if (!userInfo.gender) { // 如果用户信息中没有性别，说明是新用户，跳转到完善信息页
							uni.navigateTo({ url: './gender' }); // 在bootpage.vue中完善信息后，会存储itemobj，并跳转到主页
						} else { // 跳转到主页
							uni.setStorageSync('itemobj', { ...userInfo, id: userId }); // 存储用户信息到 itemobj，下次进入 App.vue 时可以直接跳转到主页
							uni.setStorageSync('info', { ...userInfo, id: userId }); // 存储用户信息到 itemobj，下次进入 App.vue 时可以直接跳转到主页
							uni.switchTab({ url: '/pages/tab/index' });
						}
					}
				} else {
					console.error('后端解密失败:', res);
					wx.showToast({ title: '登录失败', icon: 'none' });
				}
			},
		}
	};
</script>

<style lang="scss">
	page {
		height: 100%;
	}

	.home-page-container {
		position: relative;
		text-align: center;
		width: 100vw;
		height: 100%;

		.pop-bgc {
			position: absolute;
			top: 0;
			left: 0;
			width: 100%;
			height: 100%;
			// z-index: -1;
		}

		.popup-container {
			height: 562rpx;
			width: 544rpx;
			overflow: hidden;

			.div-popo {
				position: absolute;
				top: 0;
				left: 0;
				width: 100%;
				height: 100%;

				.pop-title {
					color: rgba(45, 49, 50, 0.8);
					font-weight: 400;
					font-size: 40rpx;
					margin-top: 78rpx;
					font-family: 'PingFang SC-Heavy, PingFang SC';
				}

				.tips-content {
					width: 448rpx;
					margin: auto;
					text-align: left;
					margin-top: 50rpx;
					line-height: 56rpx;
					color: #666;
					font-size: 28rpx;
					font-family: 'PingFang SC-Regular, PingFang SC';

					.agreement-btn {
						display: inline-block;
						color: #5b83e8;
						font-family: 'PingFang SC-Bold, PingFang SC';
						text-decoration: underline;
					}
				}

				.btn-c {
					display: flex;
					margin: auto;
					margin-top: 64rpx;
					justify-content: space-between;
					width: 428rpx;
				}
			}

			.pop-btn {
				width: 204rpx;
				height: 76rpx;
				border-radius: 38rpx;
				text-align: center;
				line-height: 76rpx;
				border: 2rpx solid #446cd2;
				color: #5b83e8;
				font-family: 'PingFang SC-Regular, PingFang SC';

				&.agree {
					border: none;
					background: linear-gradient(86deg, #c2d2f9 0%, #c5c2f3 100%);
				}
			}
		}

		.logo-img {
			width: 168rpx;
			height: 226rpx;
			margin-top: 90vw;
		}

		.start-btn {
			width: 536rpx;
			height: 84rpx;
			background: linear-gradient(86deg, #c2d2f9 0%, #c5c2f3 100%);
			margin: auto;
			border-radius: 42rpx 42rpx 42rpx 42rpx;
			line-height: 84rpx;
			color: rgba(65, 92, 158, 0.8);
			font-weight: 400;
			font-size: 28rpx;
			position: fixed;
			bottom: 15vh;
			left: 112rpx;

			font-family: 'PingFang SC-Bold, PingFang SC';
		}
	}

	.bgc-img {
		position: absolute;
		top: -80rpx;
		left: 40rpx;
		width: 670rpx;
		pointer-events: none;
		height: 100%;
	}

	.bgc-imga {
		position: absolute;
		top: -100rpx;
		left: 0rpx;
		width: 100%;
		pointer-events: none;
		height: 100%;
	}
</style>