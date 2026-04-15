import { request } from '@/utils/request'

// 用户信息表 类型定义
export interface User {
  id?: number

  avatar?:  string

  nickname?:  string

  birthDate?:  string

  height?: number

  occupation?:  string

  gender?:  string

  graduationSchool?:  string

  highestDegree?:  string

  degreeType?:  string

  bio?:  string

  hobbies?:  string

  preferences?:  string

  currentLocation?:  string

  meetingExpectation?:  string

  relationshipGoal?:  string

  maritalStatus?:  string

  hometown?:  string

  status?:  string

  authStatus?:  string

  imgList?:  string

  idCard?:  string

  createdAt?:  string

  updatedAt?:  string

}

// 用户信息表 API
export const userApi = {
  // 分页查询
  page(params: { page: number; pageSize: number; id?: number; nickname?:  string; status?:  string; authStatus?:  string }) {
    return request({ url: '/biz/user/page', method: 'get', params })
  },

  // 获取详情
  detail(id:  string) {
    return request({ url: `/biz/user/${id}`, method: 'get' })
  },

  // 新增
  create(data: User) {
    return request({ url: '/biz/user', method: 'post', data })
  },

  // 修改
  update(data: User) {
    return request({ url: '/biz/user', method: 'put', data })
  },

  // 删除
  delete(ids:  string[]) {
    return request({ url: `/biz/user/${ids.join(',')}`, method: 'delete' })
  },

  // 导出
  export(params?: { ids?:  string[]; id?: number; nickname?:  string; status?:  string; authStatus?:  string }) {
    const p: Record<string, any> = {}
    if (params?.ids?.length) p.ids = params.ids.join(',')
    if (params?.id !== undefined && params?.id !== null) p.id = params.id
    if (params?.nickname !== undefined && params?.nickname !== null) p.nickname = params.nickname
    if (params?.status !== undefined && params?.status !== null) p.status = params.status
    if (params?.authStatus !== undefined && params?.authStatus !== null) p.authStatus = params.authStatus
    return request({ url: `/biz/user/export`, method: 'get', params: p, responseType: 'blob' })
  },

  // 导入
  importData(file: File) {
    const formData = new FormData()
    formData.append('file', file)
    return request<{ success: number; fail: number; errors: string[] }>({
      url: `/biz/user/import`,
      method: 'post',
      data: formData,
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },

  // 下载导入模板
  downloadTemplate() {
    return request({ url: `/biz/user/template`, method: 'get', responseType: 'blob' })
  }
}
