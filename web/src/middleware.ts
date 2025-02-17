import { auth } from '@/auth'
import { NextResponse } from 'next/server'

const publicRoutes = [
  {
    path: '/login',
    whenAuthenticated: 'redirect',
  },
] as const

export default auth((req) => {
  const isLoggedIn = !!req.auth
  const publicRoute = publicRoutes.find(
    (route) => req.nextUrl.pathname === route.path,
  )

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
