'use client'

import { Session } from '@/@types/session'
import { Button } from '@/components/button'
import { Device } from '@/components/device'
import { Empty } from '@/components/empty'
import { NewDevice } from '@/components/modal/newDevice'
import { Row, Title, Wrapper } from '@/lib/styled/global'
import { THEME } from '@/lib/styled/theme'
import { useGetSessions } from '@/services/session/useGetSessions'
import { useState } from 'react'
import { BiTrash } from 'react-icons/bi'
import { BsPhone } from 'react-icons/bs'
import { HiPlus } from 'react-icons/hi'
import { toast } from 'react-toastify'
import { Delete, List } from './styles'
import { Confirmation } from '@/components/modal/confirmation'
import { deleteAllSessions } from '@/app/actions/sessions/deleteAllSessions'
import Skeleton from 'react-loading-skeleton'

export const Content = () => {
  const [isNewDeviceModalOpen, setIsNewDeviceModalOpen] = useState(false)
  const [isConfirmationModalOpen, setIsConfirmationModalOpen] = useState(false)

  const { data, error, refetch, isLoading } = useGetSessions()

  const handleDeleteClick = () => {
    setIsConfirmationModalOpen(true)
  }

  const handleConfirmationDelete = async () => {
    const response = await deleteAllSessions()
    if (response.success) {
      refetch()
      handleConfirmationClose()
    } else {
      toast.error(response.error)
    }
  }

  const handleConfirmationClose = () => {
    setIsConfirmationModalOpen(false)
  }

  const handleNewDeviceClick = () => {
    setIsNewDeviceModalOpen(true)
  }

  const handleNewDeviceClose = () => {
    setIsNewDeviceModalOpen(false)
  }

  if (error) {
    toast.error('Erro ao carregar dispositivos', { toastId: 'deviceError' })
  }

  return (
    <>
      <Row style={{ flex: 1, minHeight: 0 }}>
        <Wrapper style={{ flex: 3, overflow: 'auto' }}>
          <Title>Histórico de mensagens</Title>
        </Wrapper>

        <Wrapper
          style={{
            flex: 1,
            overflow: 'hidden',
            alignItems: 'center',
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
              onClick={handleDeleteClick}
            />
          </Row>

          {isLoading ? (
            Array.from({ length: 5 }).map((_, index) => (
              <Skeleton key={index} width={329} height={64} />
            ))
          ) : data?.length === 0 ? (
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
                  status={session.status}
                  id={session.id}
                />
              ))}
            </List>
          )}
        </Wrapper>
      </Row>

      {isNewDeviceModalOpen && <NewDevice onClose={handleNewDeviceClose} />}
      {isConfirmationModalOpen && (
        <Confirmation
          title="Tem certeza de que deseja desconectar todos os dispositivos?"
          confirmButtonText="Sim, desconectar dispositivos"
          cancelButtonText="Cancelar"
          content={
            <span>
              Esta ação irá desconectar <strong>TODOS</strong> os dispositivos
              em que estiver ativo e inativo. Você precisará reconectar caso
              queira enviar novas mensagens.
            </span>
          }
          onCancelButtonClick={handleConfirmationClose}
          onCloseButtonClick={handleConfirmationClose}
          onConfirmButtonClick={handleConfirmationDelete}
        />
      )}
    </>
  )
}
