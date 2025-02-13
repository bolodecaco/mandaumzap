'use client'

import { Container } from '@/components/container'
import { Header } from '@/components/header'
import { Main } from '@/lib/styled/global'

export const Content = () => {
  return (
    <Container>
      <Main>
        <Header pageTitle="Meu histórico" />
        <h1>Content</h1>
      </Main>
    </Container>
  )
}
