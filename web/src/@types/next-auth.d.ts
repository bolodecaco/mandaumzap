import 'next-auth'

import 'next-auth/jwt'

declare module 'next-auth' {
  interface Session {
    accessToken?: string
    refreshToken?: string
    idToken?: string
    uuid?: string | null
  }
}

declare module 'next-auth/jwt' {
  interface JWT {
    accessToken?: string
    refreshToken?: string
    idToken?: string
    uuid?: string | null
  }
}
