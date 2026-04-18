<template>
	<view class="content">
		<!-- 基础资料 -->
		<view class="section-title">基础资料</view>
		<view class="form-card">
			<view class="form-item" @click="openNicknamePopup">
				<text class="label">昵称</text>
				<view class="value-wrap">
					<text class="value">{{ form.nickName || '请输入' }}</text>
					<u-icon name="arrow-right" color="#999" size="14"></u-icon>
				</view>
			</view>
			<view class="form-item">
				<text class="label">年龄</text>
				<view class="value-wrap">
					<text class="value">{{ age ? age + '岁' : '' }}</text>
				</view>
			</view>
			<view class="form-item" @click="openHeightPicker">
				<text class="label">身高</text>
				<view class="value-wrap">
					<text class="value">{{ form.height ? form.height + 'cm' : '请选择' }}</text>
					<u-icon name="arrow-right" color="#999" size="14"></u-icon>
				</view>
			</view>
			<view class="form-item" @click="openCityPicker">
				<text class="label">居住城市</text>
				<view class="value-wrap">
					<text class="value">{{ form.city || '请选择' }}</text>
					<u-icon name="arrow-right" color="#999" size="14"></u-icon>
				</view>
			</view>
			<view class="form-item" @click="openEmotionalPicker">
				<text class="label">感情状态</text>
				<view class="value-wrap">
					<text class="value">{{ emotionalLabel || '请选择' }}</text>
					<u-icon name="arrow-right" color="#999" size="14"></u-icon>
				</view>
			</view>
			<view class="form-item" @click="openMarriagePicker" style="border-bottom: none;">
				<text class="label">婚姻状况</text>
				<view class="value-wrap">
					<text class="value">{{ marriageLabel || '请选择' }}</text>
					<u-icon name="arrow-right" color="#999" size="14"></u-icon>
				</view>
			</view>
		</view>

		<!-- 学历信息 -->
		<view class="section-title">学历信息</view>
		<view class="form-card">
			<view class="form-item" @click="openSchoolPopup">
				<text class="label">毕业院校</text>
				<view class="value-wrap">
					<text class="value">{{ form.school || '请输入' }}</text>
					<u-icon name="arrow-right" color="#999" size="14"></u-icon>
				</view>
			</view>
			<view class="form-item" @click="openEducationPicker">
				<text class="label">最高学历</text>
				<view class="value-wrap">
					<text class="value">{{ educationLabel || '请选择' }}</text>
					<u-icon name="arrow-right" color="#999" size="14"></u-icon>
				</view>
			</view>
			<view class="form-item" @click="openEducationalTypePicker" style="border-bottom: none;">
				<text class="label">学历类型</text>
				<view class="value-wrap">
					<text class="value">{{ educationalTypeLabel || '请选择' }}</text>
					<u-icon name="arrow-right" color="#999" size="14"></u-icon>
				</view>
			</view>
		</view>

		<view class="save-btn" @click="save">保存</view>
		<view style="height: 40rpx;"></view>

		<!-- 昵称编辑弹窗 -->
		<u-popup :show="showNicknamePopup" @close="showNicknamePopup = false" mode="bottom">
			<view class="popup-content">
				<view class="popup-header">
					<text @click="showNicknamePopup = false">取消</text>
					<text class="popup-title">编辑昵称</text>
					<text class="confirm" @click="confirmNickname">确定</text>
				</view>
				<view class="input-wrap">
					<input type="text" v-model="tempNickName" placeholder="请输入昵称" class="nickname-input" maxlength="20" />
				</view>
			</view>
		</u-popup>

		<!-- 身高选择弹窗 -->
		<u-popup :show="showHeightPicker" @close="showHeightPicker = false" mode="bottom">
			<view class="popup-content">
				<view class="popup-header">
					<text @click="showHeightPicker = false">取消</text>
					<text class="popup-title">选择身高</text>
					<text class="confirm" @click="confirmHeight">确定</text>
				</view>
				<picker-view @change="onHeightChange" class="picker-view" :indicator-style="indicatorStyle" indicator-class="indicator-class" :mask-style="maskStyle">
					<picker-view-column>
						<view class="picker-item" v-for="(item, index) in heightArr" :key="index" :class="tempHeight == item ? 'active' : ''">{{ item }}cm</view>
					</picker-view-column>
				</picker-view>
			</view>
		</u-popup>

		<!-- 感情状态选择弹窗 -->
		<u-popup :show="showEmotionalPicker" @close="showEmotionalPicker = false" mode="bottom">
			<view class="popup-content">
				<view class="popup-header">
					<text @click="showEmotionalPicker = false">取消</text>
					<text class="popup-title">选择感情状态</text>
					<text class="confirm" @click="confirmEmotional">确定</text>
				</view>
				<picker-view @change="onEmotionalChange" class="picker-view" :indicator-style="indicatorStyle" indicator-class="indicator-class" :mask-style="maskStyle">
					<picker-view-column>
						<view class="picker-item" v-for="(item, index) in emotionalOptions" :key="index" :class="tempEmotional == item.value ? 'active' : ''">{{ item.label }}</view>
					</picker-view-column>
				</picker-view>
			</view>
		</u-popup>

		<!-- 婚姻状况选择弹窗 -->
		<u-popup :show="showMarriagePicker" @close="showMarriagePicker = false" mode="bottom">
			<view class="popup-content">
				<view class="popup-header">
					<text @click="showMarriagePicker = false">取消</text>
					<text class="popup-title">选择婚姻状况</text>
					<text class="confirm" @click="confirmMarriage">确定</text>
				</view>
				<picker-view @change="onMarriageChange" class="picker-view" :indicator-style="indicatorStyle" indicator-class="indicator-class" :mask-style="maskStyle">
					<picker-view-column>
						<view class="picker-item" v-for="(item, index) in marriageOptions" :key="index" :class="tempMarriage == item.value ? 'active' : ''">{{ item.label }}</view>
					</picker-view-column>
				</picker-view>
			</view>
		</u-popup>

		<!-- 最高学历选择弹窗 -->
		<u-popup :show="showEducationPicker" @close="showEducationPicker = false" mode="bottom">
			<view class="popup-content">
				<view class="popup-header">
					<text @click="showEducationPicker = false">取消</text>
					<text class="popup-title">选择最高学历</text>
					<text class="confirm" @click="confirmEducation">确定</text>
				</view>
				<picker-view @change="onEducationChange" class="picker-view" :indicator-style="indicatorStyle" indicator-class="indicator-class" :mask-style="maskStyle">
					<picker-view-column>
						<view class="picker-item" v-for="(item, index) in educationOptions" :key="index" :class="tempEducation == item.value ? 'active' : ''">{{ item.label }}</view>
					</picker-view-column>
				</picker-view>
			</view>
		</u-popup>

		<!-- 学历类型选择弹窗 -->
		<u-popup :show="showEducationalTypePicker" @close="showEducationalTypePicker = false" mode="bottom">
			<view class="popup-content">
				<view class="popup-header">
					<text @click="showEducationalTypePicker = false">取消</text>
					<text class="popup-title">选择学历类型</text>
					<text class="confirm" @click="confirmEducationalType">确定</text>
				</view>
				<picker-view @change="onEducationalTypeChange" class="picker-view" :indicator-style="indicatorStyle" indicator-class="indicator-class" :mask-style="maskStyle">
					<picker-view-column>
						<view class="picker-item" v-for="(item, index) in educationalTypeOptions" :key="index" :class="tempEducationalType == item.value ? 'active' : ''">{{ item.label }}</view>
					</picker-view-column>
				</picker-view>
			</view>
		</u-popup>

		<!-- 毕业院校搜索弹窗 -->
		<u-popup :show="showSchoolPopup" @close="closeSchoolPopup" mode="bottom">
			<view class="popup-content" style="height: 70vh;">
				<view class="popup-header">
					<text @click="closeSchoolPopup">取消</text>
					<text class="popup-title">选择毕业院校</text>
					<text class="confirm" @click="confirmSchool">确定</text>
				</view>
				<view class="search-box">
					<view class="search-input-wrap">
						<input type="text" v-model="searchValue" placeholder="请输入学校名称" class="search-input" @input="onSchoolInput" @confirm="searchSchool" />
						<image src="/static/images/close.png" class="search-close" @click="clearSchoolSearch" mode="aspectFill" v-if="searchValue"></image>
					</view>
				</view>
				<scroll-view scroll-y class="search-result-list">
					<view class="search-result-item" v-for="(item, index) in searchResList" :key="index" @click="selectSchool(item)">
						{{ item.name }}
					</view>
					<view class="search-empty" v-if="isShowSearchList && searchResList.length == 0">
						未找到相关院校
					</view>
				</scroll-view>
			</view>
		</u-popup>

		<!-- 城市选择组件 -->
		<level-linkage ref="levelLinkage" :pickerValueDefault="pickerValueDefault" @take="onCitySelect" themeColor="#007AFF"></level-linkage>

		<ELM ref="elm" :msg="tipMsg" :isConfirm="isConfirm" @confirm="confirmToast"></ELM>
	</view>
</template>

<script>
	import levelLinkage from '../components/three-level-linkage/linkage.vue';
	import ELM from '@/components/elm-toast/index.vue';

	export default {
		components: {
			levelLinkage,
			ELM
		},
		data() {
			return {
				form: {
					nickName: '',
					birthday: '',
					height: '',
					city: '',
					emotional: '',
					marriage: '',
					school: '',
					education: '',
					educationalType: ''
				},
				age: '',
				// 弹窗显示状态
				showNicknamePopup: false,
				showHeightPicker: false,
				showEmotionalPicker: false,
				showMarriagePicker: false,
				showEducationPicker: false,
				showEducationalTypePicker: false,
				showSchoolPopup: false,
				// 临时值
				tempNickName: '',
				tempHeight: '',
				tempEmotional: '',
				tempMarriage: '',
				tempEducation: '',
				tempEducationalType: '',
				// 选项数据
				heightArr: [],
				emotionalOptions: [
					{ label: '正在寻觅', value: 'Single' },
					{ label: '热恋中', value: 'Love' }
				],
				marriageOptions: [
					{ label: '未婚', value: 'Unmarried' },
					{ label: '离异', value: 'Divorce' },
					{ label: '丧偶', value: 'Widow' }
				],
				educationOptions: [
					{ label: '博士', value: 'Doctor' },
					{ label: '硕士', value: 'Master' },
					{ label: '本科', value: 'Undergraduate' },
					{ label: '专科', value: 'Specialty' }
				],
				educationalTypeOptions: [
					{ label: '全日制', value: 'FullTime' },
					{ label: '非全日制', value: 'NOFullTime' }
				],
				// 搜索
				searchValue: '',
				searchResList: [],
				isShowSearchList: false,
				// picker 样式
				indicatorStyle: 'height: 41px;color: rgba(51, 51, 51, 0.2);',
				maskStyle: 'opacity: 0.1;background: rgba(238, 245, 254, 0);',
				pickerValueDefault: [0, 0],
				// toast
				tipMsg: '',
				isConfirm: false
			}
		},
		computed: {
			emotionalLabel() {
				const item = this.emotionalOptions.find(i => i.value === this.form.emotional);
				return item ? item.label : '';
			},
			marriageLabel() {
				const item = this.marriageOptions.find(i => i.value === this.form.marriage);
				return item ? item.label : '';
			},
			educationLabel() {
				const item = this.educationOptions.find(i => i.value === this.form.education);
				return item ? item.label : '';
			},
			educationalTypeLabel() {
				const item = this.educationalTypeOptions.find(i => i.value === this.form.educationalType);
				return item ? item.label : '';
			}
		},
		onShow() {
			this.loadUserInfo();
			this.initHeightArr();
		},
		methods: {
			initHeightArr() {
				this.heightArr = [];
				for (let i = 140; i <= 200; i++) {
					this.heightArr.push(i);
				}
			},
			getAge(birthday) {
				if (!birthday) return '';
				const birth = new Date(birthday);
				const now = new Date();
				let age = now.getFullYear() - birth.getFullYear();
				const monthDiff = now.getMonth() - birth.getMonth();
				if (monthDiff < 0 || (monthDiff === 0 && now.getDate() < birth.getDate())) {
					age--;
				}
				return age;
			},
			async loadUserInfo() {
				const info = uni.getStorageSync('info');
				const userId = uni.getStorageSync('userId');
				const res = await this.$myRequest({
					url: 'user/getInfo',
					withToken: true,
					method: 'POST',
					data: {
						id: userId
					}
				});
				if (res.data.code == 200) {
					const data = res.data.data;
					this.form.nickName = data.nickName || '';
					this.form.birthday = data.birthday || (info ? info.birthday : '');
					this.form.height = data.height || '';
					this.form.city = data.city || '';
					this.form.emotional = data.emotional || '';
					this.form.marriage = data.marriage || '';
					this.form.school = data.school || '';
					this.form.education = data.education || '';
					this.form.educationalType = data.educationalType || '';
					this.age = this.getAge(this.form.birthday);
				} else {
					this.tipMsg = res.data.msg;
					this.$refs.elm.showDialog();
				}
			},
			// 昵称
			openNicknamePopup() {
				this.tempNickName = this.form.nickName;
				this.showNicknamePopup = true;
			},
			confirmNickname() {
				this.form.nickName = this.tempNickName;
				this.showNicknamePopup = false;
			},
			// 身高
			openHeightPicker() {
				this.tempHeight = this.form.height || this.heightArr[0];
				this.showHeightPicker = true;
			},
			onHeightChange(e) {
				this.tempHeight = this.heightArr[e.detail.value[0]];
			},
			confirmHeight() {
				this.form.height = this.tempHeight;
				this.showHeightPicker = false;
			},
			// 城市
			openCityPicker() {
				this.$refs.levelLinkage.open(1);
			},
			onCitySelect(e) {
				this.form.city = e.regionName2 || '';
			},
			// 感情状态
			openEmotionalPicker() {
				this.tempEmotional = this.form.emotional || this.emotionalOptions[0].value;
				this.showEmotionalPicker = true;
			},
			onEmotionalChange(e) {
				this.tempEmotional = this.emotionalOptions[e.detail.value[0]].value;
			},
			confirmEmotional() {
				this.form.emotional = this.tempEmotional;
				this.showEmotionalPicker = false;
			},
			// 婚姻状况
			openMarriagePicker() {
				this.tempMarriage = this.form.marriage || this.marriageOptions[0].value;
				this.showMarriagePicker = true;
			},
			onMarriageChange(e) {
				this.tempMarriage = this.marriageOptions[e.detail.value[0]].value;
			},
			confirmMarriage() {
				this.form.marriage = this.tempMarriage;
				this.showMarriagePicker = false;
			},
			// 毕业院校搜索
			openSchoolPopup() {
				this.searchValue = this.form.school || '';
				this.searchResList = [];
				this.isShowSearchList = false;
				this.showSchoolPopup = true;
				if (this.searchValue) {
					this.searchSchool();
				}
			},
			closeSchoolPopup() {
				this.showSchoolPopup = false;
			},
			clearSchoolSearch() {
				this.searchValue = '';
				this.searchResList = [];
				this.isShowSearchList = false;
			},
			async searchSchool() {
				if (!this.searchValue) return;
				const res = await this.$myRequest({
					url: 'user/university',
					data: {
						name: this.searchValue
					},
					method: 'GET'
				});
				if (res.data.code == 200) {
					this.searchResList = res.data.data || [];
					this.isShowSearchList = true;
				} else {
					this.searchResList = [];
					this.isShowSearchList = true;
				}
			},
			onSchoolInput() {
				if (this.searchValue) {
					this.searchSchool();
				} else {
					this.searchResList = [];
					this.isShowSearchList = false;
				}
			},
			selectSchool(item) {
				this.form.school = item.name;
				this.showSchoolPopup = false;
			},
			confirmSchool() {
				this.form.school = this.searchValue;
				this.showSchoolPopup = false;
			},
			// 最高学历
			openEducationPicker() {
				this.tempEducation = this.form.education || this.educationOptions[0].value;
				this.showEducationPicker = true;
			},
			onEducationChange(e) {
				this.tempEducation = this.educationOptions[e.detail.value[0]].value;
			},
			confirmEducation() {
				this.form.education = this.tempEducation;
				this.showEducationPicker = false;
			},
			// 学历类型
			openEducationalTypePicker() {
				this.tempEducationalType = this.form.educationalType || this.educationalTypeOptions[0].value;
				this.showEducationalTypePicker = true;
			},
			onEducationalTypeChange(e) {
				this.tempEducationalType = this.educationalTypeOptions[e.detail.value[0]].value;
			},
			confirmEducationalType() {
				this.form.educationalType = this.tempEducationalType;
				this.showEducationalTypePicker = false;
			},
			// 保存
			async save() {
				if (!this.form.nickName) {
					this.tipMsg = '请输入昵称';
					this.$refs.elm.showDialog();
					return;
				}
				uni.showLoading({ title: '保存中...' });
				const res = await this.$myRequest({
					url: 'nostalgia/fruser/update/configInfo',
					withToken: true,
					method: 'PUT',
					data: {
						nickName: this.form.nickName,
						birthday: this.form.birthday,
						height: this.form.height,
						city: this.form.city,
						emotional: this.form.emotional,
						marriage: this.form.marriage,
						school: this.form.school,
						education: this.form.education,
						educationalType: this.form.educationalType
					}
				});
				uni.hideLoading();
				if (res.data.code == 200) {
					this.isConfirm = true;
					this.tipMsg = '保存成功';
					this.$refs.elm.showDialog();
				} else {
					this.tipMsg = res.data.msg;
					this.$refs.elm.showDialog();
				}
			},
			confirmToast() {
				this.isConfirm = false;
				if (this.tipMsg === '保存成功') {
					uni.navigateBack();
				}
			}
		}
	}
</script>

<style lang="scss">
	page {
		background-color: #F4F5F9;
	}

	.content {
		padding: 0 30rpx 30rpx;
	}

	.section-title {
		font-size: 28rpx;
		font-family: 'PingFang SC-Bold, PingFang SC';
		font-weight: bold;
		color: #434343;
		padding: 30rpx 0 20rpx 10rpx;
	}

	.form-card {
		background-color: #ffffff;
		border-radius: 20rpx;
		padding: 0 30rpx;
	}

	.form-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
		height: 100rpx;
		border-bottom: 1rpx solid #f0f0f0;

		.label {
			font-size: 28rpx;
			font-family: 'PingFang SC-Regular, PingFang SC';
			font-weight: 400;
			color: #333333;
		}

		.value-wrap {
			display: flex;
			align-items: center;

			.value {
				font-size: 28rpx;
				font-family: 'PingFang SC-Regular, PingFang SC';
				font-weight: 400;
				color: #666666;
				margin-right: 12rpx;
			}
		}
	}

	.save-btn {
		width: 686rpx;
		height: 84rpx;
		background: linear-gradient(86deg, #C2D2F9 0%, #C5C2F3 100%);
		border-radius: 42rpx;
		text-align: center;
		line-height: 84rpx;
		margin: 60rpx auto 0;
		font-size: 28rpx;
		font-family: 'PingFang SC-Bold, PingFang SC';
		font-weight: 400;
		color: rgba(65, 92, 158, 0.9);
	}

	.popup-content {
		background-color: #fff;
		border-radius: 24rpx 24rpx 0 0;
		padding-bottom: 30rpx;
	}

	.popup-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		height: 100rpx;
		padding: 0 30rpx;
		border-bottom: 1rpx solid #f0f0f0;
		font-size: 28rpx;
		color: #666;

		.popup-title {
			font-size: 32rpx;
			font-weight: bold;
			color: #333;
		}

		.confirm {
			color: #446cd2;
		}
	}

	.input-wrap {
		padding: 30rpx;

		.nickname-input {
			width: 100%;
			height: 90rpx;
			background: #F8F8F8;
			border-radius: 12rpx;
			padding: 0 24rpx;
			font-size: 28rpx;
			color: #333;
		}
	}

	.picker-view {
		height: 450rpx;
		margin: auto;
	}

	.picker-item {
		width: 100%;
		text-align: center;
		line-height: 41px;
		height: 41px;
		color: #B8B8B8;
		font-weight: 400;
		font-family: 'PingFang SC-Regular, PingFang SC';
	}

	.picker-item.active {
		color: rgba(67, 67, 67, 0.9);
		font-family: 'PingFang SC-Bold, PingFang SC';
	}

	::v-deep .indicator-class {
		background: #EEF5FE;
		z-index: -1;
	}

	// 学校搜索
	.search-box {
		padding: 20rpx 30rpx;
	}

	.search-input-wrap {
		width: 100%;
		height: 80rpx;
		background: #F8F8F8;
		border-radius: 40rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 24rpx;
	}

	.search-input {
		flex: 1;
		font-size: 28rpx;
		color: #333;
	}

	.search-close {
		width: 32rpx;
		height: 32rpx;
		margin-left: 16rpx;
	}

	.search-result-list {
		height: calc(70vh - 220rpx);
		padding: 0 30rpx;
	}

	.search-result-item {
		height: 90rpx;
		line-height: 90rpx;
		font-size: 28rpx;
		color: #333;
		border-bottom: 1rpx solid #f5f5f5;
	}

	.search-empty {
		padding: 60rpx 0;
		text-align: center;
		font-size: 28rpx;
		color: #999;
	}
</style>
