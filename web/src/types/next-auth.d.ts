import 'next-auth'

declare module 'next-auth' {
  interface Session {
    accessToken?: string
    refreshToken?: string
    idToken?: string
    uuid?: string
  }
}
