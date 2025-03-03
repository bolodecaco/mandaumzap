'use client'

import { Chat } from '@/@types/chat'
import { Sort } from '@/app/actions/chats/getAllChats'
import { Button } from '@/components/button'
import { CardContact } from '@/components/cardContact'
import { Checkbox } from '@/components/cardContact/styles'
import { Empty } from '@/components/empty'
import { Input } from '@/components/input'
import { AddToListModal } from '@/components/modal/addToList'
import { Selector } from '@/components/selector'
import { Row, Title, Wrapper } from '@/lib/styled/global'
import { useGetChats } from '@/services/chat/useGetChats'
import { useGetSessions } from '@/services/session/useGetSessions'
import { useRouter } from 'next/navigation'
import { parseAsString, useQueryState } from 'nuqs'
import { useEffect, useMemo, useState } from 'react'
import { FiSearch } from 'react-icons/fi'
import { MdGroupAdd } from 'react-icons/md'
import { useInView } from 'react-intersection-observer'
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
  const { ref, inView } = useInView()

  const [orderBy, setOrderBy] = useQueryState('orderBy')
  const [session, setSession] = useQueryState('session')
  const [search, setSearch] = useQueryState('search', parseAsString)
  const [selectedChats, setSelectedChats] = useState<string[]>([])
  const [isListsModalOpen, setIsListsModalOpen] = useState(false)

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

  const allChatsIds = useMemo(
    () =>
      chats?.pages.flatMap((page) =>
        page.content.map((chat: Chat) => chat.id),
      ) || [],
    [chats?.pages],
  )

  const isAllSelected = useMemo(
    () =>
      allChatsIds.length > 0 &&
      selectedChats.length === allChatsIds.length &&
      allChatsIds.every((id) => selectedChats.includes(id)),
    [selectedChats, allChatsIds],
  )

  const handleReconnect = () => {
    router.push('/history')
  }

  const handleSelectAll = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSelectedChats(e.target.checked ? allChatsIds : [])
  }

  const handleSelectChat = (chatId: string) => {
    setSelectedChats((prev) =>
      prev.includes(chatId)
        ? prev.filter((id) => id !== chatId)
        : [...prev, chatId],
    )
  }

  const handleSendSelectedChats = async () => {
    setIsListsModalOpen(true)
  }

  const handleSessionChange = (newValue: string) => {
    setSession(newValue)
  }

  useEffect(() => {
    if (!session && sessions?.length) {
      setSession(sessions[0].id)
    }
  }, [sessions, session, setSession])

  useEffect(() => {
    if (inView && hasNextPage && !isFetchingNextPage) {
      fetchNextPage()
    }
  }, [fetchNextPage, hasNextPage, inView, isFetchingNextPage])

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
          onValueChange={handleSessionChange}
          height="2.5rem"
        />
        {selectedChats.length > 0 && (
          <Button
            text={`Enviar ${selectedChats.length} chats selecionados`}
            onClick={handleSendSelectedChats}
          />
        )}
      </Row>

      <ListHeader>
        <Checkbox
          type="checkbox"
          checked={isAllSelected}
          onChange={handleSelectAll}
        />
        <UserDiv>Nome do chat</UserDiv>
        <Session>Contato</Session>
        <Session>Sessão</Session>
      </ListHeader>

      {!session && isLoadingChats ? (
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
        <>
          <List>
            {chats?.pages.map((page) =>
              page.content.map((data: Chat) => (
                <CardContact
                  key={data.id}
                  name={data.chatName}
                  session={data.sessionId}
                  contact={data.whatsAppId}
                  checked={selectedChats.includes(data.id)}
                  onCheck={() => handleSelectChat(data.id)}
                />
              )),
            )}
            <div ref={ref} style={{ height: '20px' }} />
            {isFetchingNextPage && (
              <LoaderContainer style={{ paddingBlock: '1rem' }}>
                <Spinner />
              </LoaderContainer>
            )}
          </List>
        </>
      )}

      {isListsModalOpen && (
        <AddToListModal
          onClose={() => setIsListsModalOpen(false)}
          chatsId={selectedChats}
          onAddedSuccessfully={() => setSelectedChats([])}
        />
      )}
    </Wrapper>
  )
}

export default Chats
