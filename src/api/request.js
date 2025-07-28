import axios from "axios"
import { Message } from "element-ui"
import store from "@/store"

// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 30000,
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    store.commit("SET_LOADING", true)

    // 检查是否是FormData类型，如果是则不设置Content-Type
    // 让浏览器自动设置，包括boundary
    if (config.data instanceof FormData) {
      if (config.headers) {
        delete config.headers["Content-Type"]
      }
      // 确保axios不会将FormData转换为其他格式
      config.transformRequest = [(data) => data];
    } else if (config.data && typeof config.data === "object" && !(config.data instanceof FormData)) {
      // 如果是普通对象，确保设置为application/json
      config.headers = config.headers || {}
      config.headers["Content-Type"] = "application/json"
    }

    return config
  },
  (error) => {
    store.commit("SET_LOADING", false)
    console.error(error)
    return Promise.reject(error)
  },
)

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    store.commit("SET_LOADING", false)
    // 未设置状态码则默认成功状态
    const code = response.data.code || 200
    // 获取错误信息
    const msg = response.data.msg || "请求错误"
    // 二进制数据则直接返回
    if (response.request.responseType === "blob" || response.request.responseType === "arraybuffer") {
      return response.data
    }
    if (code === 401) {
      Message.error(msg || "无效的会话，或者会话已过期，请重新登录。")
      return Promise.reject(new Error(msg || "无效的会话，或者会话已过期，请重新登录。"))
    } else if (code === 500) {
      Message.error(msg || "服务器内部错误")
      return Promise.reject(new Error(msg || "服务器内部错误"))
    } else if (code !== 200) {
      Message.error(msg || "请求错误")
      return Promise.reject(new Error(msg || "请求错误"))
    } else {
      return response.data
    }
  },
  (error) => {
    store.commit("SET_LOADING", false)
    console.error("请求错误:", error)
    let message = error.message || "请求错误"

    // 添加更详细的错误信息处理
    if (error.response) {
      // 服务器返回了错误状态码
      const status = error.response.status
      const data = error.response.data

      if (status === 400) {
        message = data.message || "请求参数错误"
      } else if (status === 401) {
        message = "无效的会话，或者会话已过期，请重新登录"
      } else if (status === 403) {
        message = "您没有权限执行此操作"
      } else if (status === 404) {
        message = "请求的资源不存在"
      } else if (status === 415) {
        message = "不支持的媒体类型: " + (error.response.headers["content-type"] || "未知类型")
      } else if (status >= 500) {
        message = "服务器内部错误"
      }
    } else if (message === "Network Error") {
      message = "后端接口连接异常"
    } else if (message.includes("timeout")) {
      message = "系统接口请求超时"
    } else if (message.includes("Request failed with status code")) {
      message = "系统接口" + message.substr(message.length - 3) + "异常"
    }

    Message.error(message)
    return Promise.reject(error)
  },
)

export default service
