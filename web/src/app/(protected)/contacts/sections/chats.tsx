'use client'

import { Sort } from '@/app/actions/chats/getAllChats'
import { CardContact } from '@/components/cardContact'
import { Checkbox } from '@/components/cardContact/styles'
import { Empty } from '@/components/empty'
import { Input } from '@/components/input'
import { Selector } from '@/components/selector'
import { Row, Title, Wrapper } from '@/lib/styled/global'
import { useGetChats } from '@/services/chat/useGetChats'
import { useGetSessions } from '@/services/session/useGetSessions'
import { useRouter } from 'next/navigation'
import { useQueryState } from 'nuqs'
import { useCallback, useEffect, useMemo, useState } from 'react'
import { FiSearch } from 'react-icons/fi'
import { MdGroupAdd } from 'react-icons/md'
import { toast } from 'react-toastify'
import { ListHeader, ListName, Phone, UserDiv } from './styles'

const ORDER_OPTIONS = [
  {
    id: 1,
    value: 'chatName',
    name: 'Nome (A-z)',
  },
  {
    id: 2,
    value: '-chatName',
    name: 'Nome (Z-a)',
  },
  {
    id: 3,
    value: 'whatsAppId',
    name: 'Celular (A-z)',
  },
  {
    id: 4,
    value: '-whatsAppId',
    name: 'Celular (Z-a)',
  },
]

export const Chats = () => {
  const router = useRouter()

  const [orderBy, setOrderBy] = useQueryState('orderBy')
  const [session, setSession] = useQueryState('session')

  const { data: sessions, isLoading, error } = useGetSessions()
  const {
    data: chats,
    isLoading: isLoadingChats,
    error: chatsError,
  } = useGetChats({
    sessionId: session || undefined,
    sort: (orderBy as Sort) || undefined,
  })

  const SESSION_OPTIONS = useMemo(
    () =>
      sessions?.map((session) => ({
        id: session.id,
        value: session.id,
        name: session.id,
      })),
    [sessions],
  )

  const [checkAll, setCheckAll] = useState(false)

  const handleCheckAll = useCallback(() => {
    setCheckAll((prev) => !prev)
    setDataContact((prev) =>
      prev.map((contact) => ({ ...contact, checked: !checkAll })),
    )
  }, [checkAll])

  const handleCheck = useCallback((index: number) => {
    setDataContact((prev) =>
      prev.map((contact, i) =>
        i === index ? { ...contact, checked: !contact.checked } : contact,
      ),
    )
  }, [])

  const handleReconnect = () => {
    router.push('/history')
  }

  useEffect(() => {
    if (sessions) setSession(sessions[0].id)
  }, [sessions, setSession])

  if (error) {
    toast.error(`Erro ao carregar sessões: ${error}`, {
      toastId: 'sessions',
    })
  }

  return (
    <Wrapper style={{ flex: 3, overflow: 'auto' }}>
      <Title>Meus chats</Title>
      <Row style={{ gap: '0.5rem' }}>
        <Input
          keybind="Ctrl + K"
          width="22.125rem"
          height="2.5rem"
          leftIcon={FiSearch}
          placeholder="Pesquisar chat"
        />
        <Selector
          label="Ordenar"
          options={ORDER_OPTIONS}
          value={orderBy || ''}
          onValueChange={(newValue) => setOrderBy(newValue)}
          height="2.5rem"
        />
        <Selector
          label="Sessão"
          isLoading={isLoading}
          options={SESSION_OPTIONS || []}
          value={session || ''}
          onValueChange={(newValue) => setSession(newValue)}
          height="2.5rem"
        />
      </Row>

      <ListHeader>
        <Checkbox
          type="checkbox"
          checked={checkAll}
          onChange={handleCheckAll}
        />
        <UserDiv>Nome do chat</UserDiv>
        <Phone>Celular</Phone>
        <ListName>Listas presente</ListName>
      </ListHeader>

      {chats?.length === 0 ? (
        <Empty
          message="Não há chats sincronizados"
          icon={MdGroupAdd}
          action="Tente reconectar a sessão"
          onActionClick={() => {
            handleReconnect()
          }}
        />
      ) : (
        chats?.map((data, index) => (
          <CardContact
            key={`${data.name}-${index}`}
            {...data}
            onCheck={() => handleCheck(index)}
          />
        ))
      )}
    </Wrapper>
  )
}
