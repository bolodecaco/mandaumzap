'use client'

import { Session } from '@/@types/session'
import { deleteAllSessions } from '@/app/actions/sessions/deleteAllSessions'
import { Button } from '@/components/button'
import { Device } from '@/components/device'
import { Empty } from '@/components/empty'
import { Confirmation } from '@/components/modal/confirmation'
import { NewDevice } from '@/components/modal/newDevice'
import { Column, Row, Title, Wrapper } from '@/lib/styled/global'
import { THEME } from '@/lib/styled/theme'
import { useGetSessions } from '@/services/session/useGetSessions'
import { useState } from 'react'
import { BiTrash } from 'react-icons/bi'
import { BsPhone } from 'react-icons/bs'
import { HiPlus } from 'react-icons/hi'
import Skeleton from 'react-loading-skeleton'
import { toast } from 'react-toastify'
import { Delete, Header, Headline, Line, List, Text } from './styles'
import { useGetMessages } from '@/services/message/useGetMessages'
import { useGetLists } from '@/services/list/useGetLists'
import { clearHistory } from '@/app/actions/messages/clearHistory'

export const Content = () => {
  const [isNewDeviceModalOpen, setIsNewDeviceModalOpen] = useState(false)
  const [isConfirmationModalOpen, setIsConfirmationModalOpen] = useState(false)
  const [isDeleteMessagesModalOpen, setIsDeleteMessagesModalOpen] =
    useState(false)

  const { data, error, refetch, isLoading } = useGetSessions()
  const {
    data: lists,
    listsError,
    refetch: refetchList,
    isLoading: isListsLoading,
  } = useGetLists({})
  const {
    data: messages,
    error: messagesError,
    refetch: refetchMessage,
    isLoading: isMessagesLoading,
  } = useGetMessages({ enabled: !!isListsLoading })

  const handleDeleteClick = () => {
    setIsConfirmationModalOpen(true)
  }

  const handleDeleteMessagesClick = () => {
    setIsDeleteMessagesModalOpen(true)
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

  const handleMessagesConfirmationDelete = async () => {
    const response = await clearHistory()
    if (response.success) {
      refetchMessage()
      handleMessagesConfirmationClose()
    } else {
      toast.error(response.error)
    }
  }

  const handleConfirmationClose = () => {
    setIsConfirmationModalOpen(false)
  }

  const handleMessagesConfirmationClose = () => {
    setIsDeleteMessagesModalOpen(false)
  }

  const handleNewDeviceClick = () => {
    setIsNewDeviceModalOpen(true)
  }

  const handleNewDeviceClose = () => {
    setIsNewDeviceModalOpen(false)
  }

  const findMatchingList = (listId: string) => {
    const list = lists?.content.find((list) => list.id === listId)
    return list
  }

  if (error) {
    toast.error('Erro ao carregar dispositivos. Tente recarregar a página', {
      toastId: 'deviceError',
    })
  }

  if (messagesError) {
    toast.error('Erro ao carregar mensagens. Tente recarregar a página', {
      toastId: 'messagesError',
    })
  }

  return (
    <>
      <Row style={{ flex: 1, minHeight: 0 }}>
        <Wrapper style={{ flex: 3, overflow: 'auto' }}>
          <Title>Histórico de mensagens</Title>
          <Delete
            text="Remover mensagens"
            leftIcon={BiTrash}
            style={{
              alignSelf: 'flex-start',
            }}
            iconColor={THEME.colors.tertiary}
            variant="ghost"
            onClick={handleDeleteMessagesClick}
          />

          <Column style={{ width: '100%', height: '100%' }}>
            <Header>
              <Headline style={{ flex: 3 }}>Mensagem</Headline>
              <Headline style={{ flex: 1 }}>Destinatário</Headline>
              <Headline style={{ flex: 1 }}>Enviado em</Headline>
            </Header>
            <List style={{ marginLeft: 0 }}>
              {!isMessagesLoading &&
                messages.map((message) => {
                  const matchingList = findMatchingList(message.broadcastListId)
                  return (
                    <Line key={message.id}>
                      <Text style={{ flex: 3 }}>{message.content}</Text>
                      <Text style={{ flex: 1 }}>{matchingList.title}</Text>
                      <Text style={{ flex: 1 }}>Ontem, 11:26</Text>
                    </Line>
                  )
                })}
            </List>
          </Column>
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

          {data?.length === 0 ? (
            <Empty
              message={'Nenhum dispositivo conectado'}
              icon={BsPhone}
              action="Conectar novo dispositivo"
              onActionClick={handleNewDeviceClick}
            />
          ) : (
            <List>
              {isLoading
                ? Array.from({ length: 5 }).map((_, index) => (
                    <Skeleton key={index} height={64} />
                  ))
                : data?.map((session: Session) => (
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
      {isDeleteMessagesModalOpen && (
        <Confirmation
          title="Tem certeza de que deseja excluir todas as mensagens?"
          confirmButtonText="Sim, excluir mensagens"
          cancelButtonText="Cancelar"
          content={
            <span>
              Esta ação irá excluir <strong>TODAS</strong> as mensagens do
              histórico. É uma ação irreversível.
            </span>
          }
          onCancelButtonClick={handleMessagesConfirmationClose}
          onCloseButtonClick={handleMessagesConfirmationClose}
          onConfirmButtonClick={handleMessagesConfirmationDelete}
        />
      )}
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
