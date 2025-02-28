'use client'

import { Chat } from '@/@types/chat'
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
import { parseAsString, useQueryState } from 'nuqs'
import { useEffect, useMemo, useRef } from 'react'
import { FiSearch } from 'react-icons/fi'
import { MdGroupAdd } from 'react-icons/md'
import { toast } from 'react-toastify'
import {
  List,
  ListHeader,
  LoaderContainer,
  Session,
  Spinner,
  UserDiv,
} from './styles'

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

const Chats = () => {
  const router = useRouter()
  const observerTarget = useRef<HTMLDivElement>(null)

  const [orderBy, setOrderBy] = useQueryState('orderBy')
  const [session, setSession] = useQueryState('session')
  const [search, setSearch] = useQueryState('search', parseAsString)

  const { data: sessions, isLoading, error } = useGetSessions()
  const {
    data: chats,
    isLoading: isLoadingChats,
    error: chatsError,
    hasNextPage,
    fetchNextPage,
    isFetchingNextPage,
  } = useGetChats({
    sessionId: session || undefined,
    sort: (orderBy as Sort) || undefined,
    search: search || undefined,
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

  const handleReconnect = () => {
    router.push('/history')
  }

  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && hasNextPage && !isFetchingNextPage) {
          fetchNextPage()
        }
      },
      { threshold: 0.1 },
    )

    if (observerTarget.current) {
      observer.observe(observerTarget.current)
    }

    return () => observer.disconnect()
  }, [hasNextPage, isFetchingNextPage, fetchNextPage])

  useEffect(() => {
    if (sessions) setSession(sessions[0].id)
  }, [sessions, setSession])

  if (error) {
    toast.error(`Erro ao carregar sessões: ${error}`, {
      toastId: 'sessions',
    })
  }

  if (chatsError) {
    toast.error(`Erro ao carregar chats: ${chatsError}`, {
      toastId: 'chats',
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
          value={search || ''}
          onChange={(e) => setSearch(e.target.value)}
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
        <Checkbox type="checkbox" />
        <UserDiv>Nome do chat</UserDiv>
        <Session>Contato</Session>
        <Session>Sessão</Session>
      </ListHeader>

      {isLoadingChats ? (
        <LoaderContainer>
          <Spinner />
        </LoaderContainer>
      ) : !chats?.pages[0]?.content.length ? (
        <Empty
          message="Não há chats sincronizados"
          icon={MdGroupAdd}
          action="Tente reconectar a sessão"
          onActionClick={handleReconnect}
        />
      ) : (
        <List>
          {chats?.pages.map((page) =>
            page.content.map((data: Chat) => (
              <CardContact
                key={data.id}
                name={data.chatName}
                session={data.sessionId}
                contact={data.whatsAppId}
              />
            )),
          )}
          <div ref={observerTarget} style={{ height: '20px' }} />
          {isFetchingNextPage && (
            <LoaderContainer style={{ paddingBlock: '1rem' }}>
              <Spinner />
            </LoaderContainer>
          )}
        </List>
      )}
    </Wrapper>
  )
}

export default Chats
