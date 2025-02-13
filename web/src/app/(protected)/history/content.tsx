'use client'

import { Container } from '@/components/container'
import { Header } from '@/components/header'
import { Main, Row, Title, Wrapper } from '@/lib/styled/global'

export const Content = () => {
  return (
    <Container>
      <Main>
        <Header pageTitle="Meu histórico" />
        <Row style={{ height: '100%' }}>
          <Wrapper style={{ flex: 2 }}>
            <Title>Histórico de mensagens</Title>
          </Wrapper>
          <Wrapper style={{ flex: 1 }}>
            <Title>Histórico de dispositivos</Title>
          </Wrapper>
        </Row>
      </Main>
    </Container>
  )
}
