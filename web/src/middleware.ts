import { auth } from '@/auth'
import { NextResponse } from 'next/server'
import { jwtDecode } from 'jwt-decode'

const publicRoutes = [
  {
    path: '/login',
    whenAuthenticated: 'redirect',
  },
] as const

export default auth((req) => {
  const isLoggedIn = !!req.auth
  const accessToken = isLoggedIn && req.auth?.accessToken
  const refreshToken = isLoggedIn && req.auth?.refreshToken
  const publicRoute = publicRoutes.find(
    (route) => req.nextUrl.pathname === route.path,
  )

  if (!publicRoute && isLoggedIn && accessToken) {
    try {
      const decodedToken = jwtDecode(accessToken)
      const expirationTime = (decodedToken.exp || 0) * 1000 // Convert to milliseconds

      if (Date.now() >= expirationTime && !refreshToken) {
        const callbackUrl = encodeURIComponent(req.nextUrl.pathname)
        return NextResponse.redirect(
          new URL(`/login?callbackUrl=${callbackUrl}`, req.url),
        )
      }
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
    } catch (error) {
      const callbackUrl = encodeURIComponent(req.nextUrl.pathname)
      return NextResponse.redirect(
        new URL(`/login?callbackUrl=${callbackUrl}`, req.url),
      )
    }
  }

  if (!publicRoute && !isLoggedIn) {
    const callbackUrl = encodeURIComponent(req.nextUrl.pathname)
    return NextResponse.redirect(
      new URL(`/login?callbackUrl=${callbackUrl}`, req.url),
    )
  }

  if (
    publicRoute &&
    isLoggedIn &&
    publicRoute.whenAuthenticated === 'redirect'
  ) {
    const callbackUrl = req.nextUrl.searchParams.get('callbackUrl')
    if (callbackUrl) {
      return NextResponse.redirect(new URL(callbackUrl, req.url))
    }
    return NextResponse.redirect(new URL('/dashboard', req.url))
  }

  return NextResponse.next()
})

export const config = {
  matcher: ['/((?!api|_next/static|_next/image|favicon.ico).*)'],
}
