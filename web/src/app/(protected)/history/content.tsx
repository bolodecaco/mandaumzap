'use client'

import { Button } from '@/components/button'
import { Container } from '@/components/container'
import { Header } from '@/components/header'
import { NewDevice } from '@/components/modal/newDevice'
import { Main, Row, Title, Wrapper } from '@/lib/styled/global'
import { THEME } from '@/lib/styled/theme'
import { useState } from 'react'
import { BiTrash } from 'react-icons/bi'
import { HiPlus } from 'react-icons/hi'
import { Delete } from './styles'

export const Content = () => {
  const [isModalOpen, setIsModalOpen] = useState(false)

  const handleNewDeviceClick = () => {
    setIsModalOpen(true)
  }

  const handleModalClose = () => {
    setIsModalOpen(false)
  }

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
            <Row style={{ gap: '0.5rem' }}>
              <Button
                text="Novo dispositivo"
                leftIcon={HiPlus}
                onClick={handleNewDeviceClick}
              />
              <Delete
                text="Remover todos"
                leftIcon={BiTrash}
                iconColor={THEME.colors.tertiary}
                variant="ghost"
              />
            </Row>
          </Wrapper>
        </Row>
      </Main>

      {isModalOpen && <NewDevice onClose={handleModalClose} />}
    </Container>
  )
}
