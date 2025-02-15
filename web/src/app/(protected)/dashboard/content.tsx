'use client'

import { Container } from '@/components/container'
import { Header } from '@/components/header'
import { Main } from '@/lib/styled/global'
import { signOut } from 'next-auth/react'

export function Content() {
  return (
    <Container>
      <Main>
        <Header pageTitle="Dashboard" />
        <button onClick={() => signOut()}>Logout</button>
      </Main>
    </Container>
  )
}
