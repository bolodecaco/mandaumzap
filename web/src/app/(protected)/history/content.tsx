'use client'

import { Session } from '@/@types/session'
import { Button } from '@/components/button'
import { Container } from '@/components/container'
import { Device } from '@/components/device'
import { Empty } from '@/components/empty'
import { Header } from '@/components/header'
import { NewDevice } from '@/components/modal/newDevice'
import { Main, Row, Title, Wrapper } from '@/lib/styled/global'
import { THEME } from '@/lib/styled/theme'
import { useGetSessions } from '@/services/session/useGetSessions'
import { useState } from 'react'
import { BiTrash } from 'react-icons/bi'
import { BsPhone } from 'react-icons/bs'
import { HiPlus } from 'react-icons/hi'
import { toast } from 'react-toastify'
import { Delete, List } from './styles'

export const Content = () => {
  const [isModalOpen, setIsModalOpen] = useState(false)

  const { data, error } = useGetSessions()

  const handleNewDeviceClick = () => {
    setIsModalOpen(true)
  }

  const handleModalClose = () => {
    setIsModalOpen(false)
  }

  if (error) {
    toast.error('Erro ao carregar dispositivos', { toastId: 'deviceError' })
  }

  return (
    <Container>
      <Main>
        <Header pageTitle="Meu histórico" />
        <Row style={{ flex: 1, minHeight: 0 }}>
          <Wrapper style={{ flex: 3, overflow: 'auto' }}>
            <Title>Histórico de mensagens</Title>
          </Wrapper>

          <Wrapper
            style={{
              flex: 1,
              overflow: 'hidden',
              alignItems: 'center',
              justifyContent: 'center',
            }}
          >
            <Title style={{ width: '100%' }}>Histórico de dispositivos</Title>
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

            {data?.length === 0 ? (
              <Empty
                message={'Nenhum dispositivo conectado'}
                icon={BsPhone}
                action="Conectar novo dispositivo"
                onActionClick={handleNewDeviceClick}
              />
            ) : (
              <List>
                {data?.map((session: Session) => (
                  <Device
                    key={session.id}
                    active={session.active}
                    id={session.id}
                  />
                ))}
              </List>
            )}
          </Wrapper>
        </Row>
      </Main>

      {isModalOpen && <NewDevice onClose={handleModalClose} />}
    </Container>
  )
}
