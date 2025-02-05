'use client'

import { Page } from '@/lib/styled/global'
import { signIn, signOut, useSession } from 'next-auth/react'

export default function Home() {
  const { data: session, status } = useSession()
  console.log(session?.accessToken)
  return (
    <Page
      style={{
        alignItems: 'center',
        justifyContent: 'center',
        flexDirection: 'column',
      }}
    >
      {session?.user && <span>Olá, {session.user.name}</span>}
      {status === 'authenticated' ? (
        <button onClick={() => signOut()}>Logout</button>
      ) : (
        <button onClick={() => signIn('keycloak')}>Login</button>
      )}
    </Page>
  )
}
