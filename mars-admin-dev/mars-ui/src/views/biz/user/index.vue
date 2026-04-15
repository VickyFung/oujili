<template>
  <div class="page-container">
    <n-card>
      <!-- 搜索表单 -->
      <div class="search-form">
        <n-form inline :model="searchForm" label-placement="left">
          <n-form-item label="用户ID">
            <n-input v-model:value="searchForm.id" placeholder="请输入用户ID" clearable />
          </n-form-item>
          <n-form-item label="昵称">
            <n-input v-model:value="searchForm.nickname" placeholder="请输入昵称" clearable />
          </n-form-item>
          <n-form-item label="用户状态">
            <n-select v-model:value="searchForm.status" placeholder="请选择用户状态" clearable style="width: 150px" :options="statusOptions" />
          </n-form-item>
          <n-form-item label="用户认证状态">
            <n-select v-model:value="searchForm.authStatus" placeholder="请选择用户认证状态" clearable style="width: 150px" :options="authStatusOptions" />
          </n-form-item>
          <n-form-item>
            <n-space>
              <n-button type="primary" @click="handleSearch">
                <template #icon><n-icon><SearchOutline /></n-icon></template>
                搜索
              </n-button>
              <n-button @click="handleReset">
                <template #icon><n-icon><RefreshOutline /></n-icon></template>
                重置
              </n-button>
            </n-space>
          </n-form-item>
        </n-form>
      </div>

      <!-- 工具栏 -->
      <div class="table-toolbar">
        <n-space>
          <n-button type="primary" @click="handleAdd">
            <template #icon><n-icon><AddOutline /></n-icon></template>
            新增
          </n-button>
          <n-button @click="importModalVisible = true">
            <template #icon><n-icon><CloudUploadOutline /></n-icon></template>
            导入
          </n-button>
          <n-button @click="handleExport">
            <template #icon><n-icon><DownloadOutline /></n-icon></template>
            导出{{ selectedIds.length > 0 ? `(${selectedIds.length})` : '' }}
          </n-button>
          <n-button type="error" :disabled="selectedIds.length === 0" @click="handleBatchDelete">
            <template #icon><n-icon><TrashOutline /></n-icon></template>
            删除
          </n-button>
        </n-space>
      </div>

      <!-- 表格 -->
      <n-data-table
        :columns="columns"
        :data="tableData"
        :loading="loading"
        :pagination="pagination"
        :row-key="(row) => row.id"
        :scroll-x="1200"
        @update:page="handlePageChange"
        @update:page-size="handlePageSizeChange"
        @update:checked-row-keys="handleCheck"
      />
    </n-card>

    <!-- 新增/编辑弹窗 -->
    <n-modal v-model:show="modalVisible" preset="card" :title="modalTitle" style="width: 800px">
      <n-form ref="formRef" :model="formData" :rules="formRules" label-placement="left" label-width="100px">
        <n-grid :cols="2" :x-gap="24">
        <n-form-item-gi label="头像" path="avatar">
          <ImageUpload v-model="formData.avatar" />
        </n-form-item-gi>
        <n-form-item-gi label="昵称" path="nickname">
          <n-input v-model:value="formData.nickname" placeholder="请输入昵称" />
        </n-form-item-gi>
        <n-form-item-gi label="出生日期" path="birthDate">
          <n-date-picker v-model:value="formData.birthDate" type="date"  clearable style="width: 100%" />
        </n-form-item-gi>
        <n-form-item-gi label="身高" path="height">
          <n-input v-model:value="formData.height" placeholder="请输入身高" />
        </n-form-item-gi>
        <n-form-item-gi label="职业" path="occupation">
          <n-input v-model:value="formData.occupation" placeholder="请输入职业" />
        </n-form-item-gi>
        <n-form-item-gi label="性别" path="gender">
          <n-select v-model:value="formData.gender" placeholder="请选择性别" :options="genderOptions" />
        </n-form-item-gi>
        <n-form-item-gi label="毕业学校" path="graduationSchool">
          <n-input v-model:value="formData.graduationSchool" placeholder="请输入毕业学校" />
        </n-form-item-gi>
        <n-form-item-gi label="最高学历" path="highestDegree">
          <n-select v-model:value="formData.highestDegree" placeholder="请选择最高学历" :options="highestDegreeOptions" />
        </n-form-item-gi>
        <n-form-item-gi label="学历类型" path="degreeType">
          <n-select v-model:value="formData.degreeType" placeholder="请选择学历类型" :options="degreeTypeOptions" />
        </n-form-item-gi>
        <n-form-item-gi label="个人介绍" path="bio">
          <n-input v-model:value="formData.bio" placeholder="请输入个人介绍" />
        </n-form-item-gi>
        <n-form-item-gi label="兴趣爱好" path="hobbies">
          <n-input v-model:value="formData.hobbies" placeholder="请输入兴趣爱好" />
        </n-form-item-gi>
        <n-form-item-gi label="择偶要求" path="preferences">
          <n-input v-model:value="formData.preferences" placeholder="请输入择偶要求" />
        </n-form-item-gi>
        <n-form-item-gi label="当前位置" path="currentLocation">
          <n-input v-model:value="formData.currentLocation" placeholder="请输入当前位置" />
        </n-form-item-gi>
        <n-form-item-gi label="见面预期" path="meetingExpectation">
          <n-input v-model:value="formData.meetingExpectation" placeholder="请输入见面预期" />
        </n-form-item-gi>
        <n-form-item-gi label="期望关系" path="relationshipGoal">
          <n-select v-model:value="formData.relationshipGoal" placeholder="请选择期望关系" :options="relationshipGoalOptions" />
        </n-form-item-gi>
        <n-form-item-gi label="情感状态" path="maritalStatus">
          <n-select v-model:value="formData.maritalStatus" placeholder="请选择情感状态" :options="maritalStatusOptions" />
        </n-form-item-gi>
        <n-form-item-gi label="家乡" path="hometown">
          <n-input v-model:value="formData.hometown" placeholder="请输入家乡" />
        </n-form-item-gi>
        <n-form-item-gi label="用户状态" path="status">
          <n-select v-model:value="formData.status" placeholder="请选择用户状态" :options="statusOptions" />
        </n-form-item-gi>
        <n-form-item-gi label="用户认证状态" path="authStatus">
          <n-select v-model:value="formData.authStatus" placeholder="请选择用户认证状态" :options="authStatusOptions" />
        </n-form-item-gi>
        <n-form-item-gi label="身份证号" path="idCard">
          <n-input v-model:value="formData.idCard" placeholder="请输入身份证号" />
        </n-form-item-gi>
        </n-grid>
      </n-form>
      <template #footer>
        <n-space justify="end">
          <n-button @click="modalVisible = false">取消</n-button>
          <n-button type="primary" @click="handleSubmit">确定</n-button>
        </n-space>
      </template>
    </n-modal>

    <!-- 导入弹窗 -->
    <n-modal v-model:show="importModalVisible" preset="card" title="导入用户信息表" style="width: 500px">
      <n-space vertical>
        <n-alert type="info">
          <template #header>导入说明</template>
          <ul style="margin: 0; padding-left: 16px; line-height: 1.8">
            <li>请先下载导入模板，按模板格式填写数据</li>
            <li>支持 .xlsx 或 .xls 格式</li>
          </ul>
        </n-alert>
        <n-space>
          <n-button type="primary" @click="handleDownloadTemplate">
            <template #icon><n-icon><DownloadOutline /></n-icon></template>
            下载模板
          </n-button>
        </n-space>
        <n-upload :max="1" accept=".xlsx,.xls" :show-file-list="true" :custom-request="handleImportUpload">
          <n-upload-dragger>
            <div style="margin-bottom: 12px">
              <n-icon size="48" :depth="3"><CloudUploadOutline /></n-icon>
            </div>
            <n-text style="font-size: 16px">点击或拖拽文件到此处上传</n-text>
            <n-p depth="3" style="margin: 8px 0 0 0">支持 .xlsx 或 .xls 格式</n-p>
          </n-upload-dragger>
        </n-upload>
      </n-space>
      <template #footer>
        <n-button @click="importModalVisible = false">关闭</n-button>
      </template>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, h, onMounted } from 'vue'
import { NButton, NSpace, NIcon, NUpload, useMessage, useDialog, type DataTableColumns, type UploadCustomRequestOptions } from 'naive-ui'
import { SearchOutline, RefreshOutline, AddOutline, TrashOutline, CreateOutline, CloudUploadOutline, DownloadOutline } from '@vicons/ionicons5'
import { userApi, type User } from '@/api/user'
import ImageUpload from '@/components/ImageUpload.vue'
import { dictDataApi } from '@/api/org'

const message = useMessage()
const dialog = useDialog()

// 搜索表单
const searchForm = reactive({
  id:  null as number | null,
  nickname: '',
  status: '',
  authStatus: '',
})

// 表格数据
const tableData = ref<User[]>([])
const loading = ref(false)
const selectedIds = ref<number[]>([])
const pagination = reactive({
  page: 1,
  pageSize: 10,
  itemCount: 0,
  showSizePicker: true,
  pageSizes: [10, 20, 50]
})

// 弹窗
const modalVisible = ref(false)
const modalTitle = ref('')
const importModalVisible = ref(false)
const formRef = ref()
const defaultFormData: User = {
  avatar: '',
  nickname: '',
  birthDate:  '',
  height: undefined,
  occupation: '',
  gender: '',
  graduationSchool: '',
  highestDegree: '',
  degreeType: '',
  bio: '',
  hobbies: '',
  preferences: '',
  currentLocation: '',
  meetingExpectation: '',
  relationshipGoal: '',
  maritalStatus: '',
  hometown: '',
  status: '',
  authStatus: '',
  idCard: '',
}
const formData = reactive<User>({ ...defaultFormData })

// 字典选项（下拉框/单选框/复选框关联字典时使用）
const genderOptions = ref<{ label: string; value: any }[]>([])
const highestDegreeOptions = ref<{ label: string; value: any }[]>([])
const degreeTypeOptions = ref<{ label: string; value: any }[]>([])
const relationshipGoalOptions = ref<{ label: string; value: any }[]>([])
const maritalStatusOptions = ref<{ label: string; value: any }[]>([])
const statusOptions = ref<{ label: string; value: any }[]>([])
const authStatusOptions = ref<{ label: string; value: any }[]>([])

// 表单校验规则
const formRules = {
  nickname: { required: true, message: '请输入昵称', trigger: 'blur' },
  gender: { required: true, message: '请输入性别', trigger: 'blur' },
  maritalStatus: { required: true, message: '请输入情感状态', trigger: 'blur' },
}

// 表格列
const columns: DataTableColumns<User> = [
  { type: 'selection' },
  { title: '用户ID', key: 'id' },
  { 
    title: '头像', 
    key: 'avatar',
    width: 100,
    render(row) {
      return row.avatar ? h('img', { 
        src: row.avatar, 
        style: { width: '60px', height: '60px', objectFit: 'cover', borderRadius: '4px' } 
      }) : '-'
    }
  },
  { title: '昵称', key: 'nickname' },
  { title: '职业', key: 'occupation' },
  { title: '性别', key: 'gender',
    render(row) {
      const val = row.gender
      const opt = genderOptions.value.find(o => o.value === val || String(o.value) === String(val))
      return opt ? opt.label : (val ?? '-')
    }
  },
  { title: '最高学历', key: 'highestDegree',
    render(row) {
      const val = row.highestDegree
      const opt = highestDegreeOptions.value.find(o => o.value === val || String(o.value) === String(val))
      return opt ? opt.label : (val ?? '-')
    }
  },
  { title: '用户状态', key: 'status',
    render(row) {
      const val = row.status
      const opt = statusOptions.value.find(o => o.value === val || String(o.value) === String(val))
      return opt ? opt.label : (val ?? '-')
    }
  },
  { title: '用户认证状态', key: 'authStatus',
    render(row) {
      const val = row.authStatus
      const opt = authStatusOptions.value.find(o => o.value === val || String(o.value) === String(val))
      return opt ? opt.label : (val ?? '-')
    }
  },
  { title: '创建时间', key: 'createdAt' },
  {
    title: '操作',
    key: 'actions',
    width: 140,
    fixed: 'right',
    render(row) {
      return h('div', { style: { display: 'flex', alignItems: 'center', gap: '8px', flexWrap: 'nowrap' } }, [
        h(NButton, { size: 'small', quaternary: true, onClick: () => handleEdit(row) }, {
          default: () => [h(NIcon, null, { default: () => h(CreateOutline) }), ' 编辑']
        }),
        h(NButton, { size: 'small', quaternary: true, type: 'error', onClick: () => handleDelete(row) }, {
          default: () => [h(NIcon, null, { default: () => h(TrashOutline) }), ' 删除']
        })
      ])
    }
  }
]

// 加载数据
async function loadData() {
  loading.value = true
  try {
    const res = await userApi.page({
      page: pagination.page,
      pageSize: pagination.pageSize,
      id: searchForm.id || undefined,
      nickname: searchForm.nickname || undefined,
      status: searchForm.status || undefined,
      authStatus: searchForm.authStatus || undefined,
    })
    tableData.value = res.list
    pagination.itemCount = res.total
  } finally {
    loading.value = false
  }
}

// 搜索
function handleSearch() {
  pagination.page = 1
  loadData()
}

// 重置
function handleReset() {
  searchForm.id =  null

  searchForm.nickname = ''

  searchForm.status = ''

  searchForm.authStatus = ''

  handleSearch()
}

// 分页
function handlePageChange(page: number) {
  pagination.page = page
  loadData()
}

function handlePageSizeChange(pageSize: number) {
  pagination.pageSize = pageSize
  pagination.page = 1
  loadData()
}

// 选择
function handleCheck(keys: Array<string | number>) {
  selectedIds.value = keys as number[]
}

// 新增
function handleAdd() {
  modalTitle.value = '新增用户信息表'
  Object.assign(formData, defaultFormData)
  modalVisible.value = true
}

// 编辑
function handleEdit(row: User) {
  modalTitle.value = '编辑用户信息表'
  Object.assign(formData, row)
  if (formData.birthDate && typeof formData.birthDate === 'string') {
    formData.birthDate = new Date(formData.birthDate + 'T00:00:00').getTime()
  }
  if (formData.createdAt && typeof formData.createdAt === 'string') {
    formData.createdAt = new Date(formData.createdAt.replace(' ', 'T')).getTime()
  }
  if (formData.updatedAt && typeof formData.updatedAt === 'string') {
    formData.updatedAt = new Date(formData.updatedAt.replace(' ', 'T')).getTime()
  }
  modalVisible.value = true
}

// 提交
async function handleSubmit() {
  await formRef.value?.validate()
  try {
    const submitData = { ...formData } as User
    submitData.avatar = formData.avatar ?? ''
    // 提交函数中的转换逻辑
    if (typeof submitData.birthDate === 'number') {
      const d = new Date(submitData.birthDate)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      submitData.birthDate = `${year}-${month}-${day}`
    }
    submitData.imgList = formData.imgList ?? ''
    if (typeof submitData.createdAt === 'number') {
      submitData.createdAt = new Date(submitData.createdAt).toISOString().slice(0, 19).replace('T', ' ')
    }
    if (typeof submitData.updatedAt === 'number') {
      submitData.updatedAt = new Date(submitData.updatedAt).toISOString().slice(0, 19).replace('T', ' ')
    }
    if (submitData.id) {
      await userApi.update(submitData)
      message.success('修改成功')
    } else {
      await userApi.create(submitData)
      message.success('新增成功')
    }
    modalVisible.value = false
    loadData()
  } catch (error) {
    // 错误已在拦截器处理
  }
}

// 删除
function handleDelete(row: User) {
  dialog.warning({
    title: '提示',
    content: '确定要删除该记录吗？',
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await userApi.delete([row.id!])
        message.success('删除成功')
        loadData()
      } catch (error) {
        // 错误已在拦截器处理
      }
    }
  })
}

// 批量删除
function handleBatchDelete() {
  dialog.warning({
    title: '提示',
    content: `确定要删除选中的 ${selectedIds.value.length} 条记录吗？`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await userApi.delete(selectedIds.value)
        message.success('删除成功')
        selectedIds.value = []
        loadData()
      } catch (error) {
        // 错误已在拦截器处理
      }
    }
  })
}

// 导出
async function handleExport() {
  try {
    const params: Record<string, any> = {}
    if (selectedIds.value.length > 0) params.ids = selectedIds.value
    if (searchForm.id != null) params.id = searchForm.id
    if (searchForm.nickname) params.nickname = searchForm.nickname
    if (searchForm.status) params.status = searchForm.status
    if (searchForm.authStatus) params.authStatus = searchForm.authStatus
    const blob = await userApi.export(params)
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = '用户信息表数据.xlsx'
    link.click()
    window.URL.revokeObjectURL(url)
  } catch (error) {
    // 错误已在拦截器处理
  }
}

// 下载导入模板
async function handleDownloadTemplate() {
  try {
    const blob = await userApi.downloadTemplate()
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = '用户信息表导入模板.xlsx'
    link.click()
    window.URL.revokeObjectURL(url)
  } catch (error) {
    // 错误已在拦截器处理
  }
}

// 导入上传
async function handleImportUpload({ file }: UploadCustomRequestOptions) {
  if (!file.file) return
  try {
    const result = await userApi.importData(file.file)
    if (result.fail > 0) {
      dialog.warning({
        title: '导入结果',
        content: `成功: ${result.success} 条，失败: ${result.fail} 条\n错误信息: ${(result.errors || []).join('\n') || '无'}`,
        positiveText: '确定'
      })
    } else {
      message.success(`导入成功，共 ${result.success} 条数据`)
      importModalVisible.value = false
    }
    loadData()
  } catch (error) {
    // 错误已在拦截器处理
  }
}

// 加载字典选项
async function loadDictOptions() {
  try {
    const data = await dictDataApi.listByType('sex')
    genderOptions.value = data.map(d => ({ label: d.dictLabel, value: d.dictValue }))
  } catch {}
  try {
    const data = await dictDataApi.listByType('degree')
    highestDegreeOptions.value = data.map(d => ({ label: d.dictLabel, value: d.dictValue }))
  } catch {}
  try {
    const data = await dictDataApi.listByType('degree_type')
    degreeTypeOptions.value = data.map(d => ({ label: d.dictLabel, value: d.dictValue }))
  } catch {}
  try {
    const data = await dictDataApi.listByType('relationship_goal')
    relationshipGoalOptions.value = data.map(d => ({ label: d.dictLabel, value: d.dictValue }))
  } catch {}
  try {
    const data = await dictDataApi.listByType('marital_status')
    maritalStatusOptions.value = data.map(d => ({ label: d.dictLabel, value: d.dictValue }))
  } catch {}
  try {
    const data = await dictDataApi.listByType('status')
    statusOptions.value = data.map(d => ({ label: d.dictLabel, value: d.dictValue }))
  } catch {}
  try {
    const data = await dictDataApi.listByType('auth_status')
    authStatusOptions.value = data.map(d => ({ label: d.dictLabel, value: d.dictValue }))
  } catch {}
}

onMounted(() => {
  loadData()
  loadDictOptions()
})
</script>

<style scoped>
.search-form {
  margin-bottom: 16px;
}

.table-toolbar {
  margin-bottom: 16px;
}
</style>
