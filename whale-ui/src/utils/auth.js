import Cookies from 'js-cookie'


const AccessTokenKey = 'entfrm_access_token'
const RefreshTokenKey = 'entfrm_refresh_token'

export function getAccessToken() {
  return Cookies.get(AccessTokenKey)
}

export function setAccessToken(token) {
  return Cookies.set(AccessTokenKey, token)
}

export function removeAccessToken() {
  return Cookies.remove(AccessTokenKey)
}

export function getRefreshToken() {
  return Cookies.get(RefreshTokenKey)
}

export function setRefreshToken(token) {
  return Cookies.set(RefreshTokenKey, token)
}

export function removeRefreshToken() {
  return Cookies.remove(RefreshTokenKey)
}
