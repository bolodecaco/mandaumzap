import { List } from '@/@types/list'
import {
  LoaderContainer,
  Spinner,
} from '@/app/(protected)/contacts/sections/styles'
import { CardList } from '@/components/cardList'
import { Input } from '@/components/input'
import { Column, Wrapper } from '@/lib/styled/global'
import { THEME } from '@/lib/styled/theme'
import { useGetLists } from '@/services/list/useGetLists'
import { useState } from 'react'
import { FiSearch, FiX } from 'react-icons/fi'
import { Background, Close, Header, StyledTitle } from '../styles'
import { toast } from 'react-toastify'
import { addChatsToList } from '@/app/actions/lists/addChatsToList'

interface AddToListModalProps {
  onClose: () => void
  chatsId: string[]
  onAddedSuccessfully: () => void
}

export const AddToListModal = ({
  onClose,
  chatsId,
  onAddedSuccessfully,
}: AddToListModalProps) => {
  const [search, setSearch] = useState('')
  const { data, isLoading } = useGetLists({ search })

  const handleAddChatsToList = async (listId: string) => {
    const formattedChats = chatsId.map((id: string) => ({ chatId: id }))
    const response = await addChatsToList({
      chats: formattedChats,
      listId,
    })

    if (response.success) {
      toast.success('Sucesso ao adicionar chats selecionados')
      onAddedSuccessfully()
      return
    }

    toast.error(`Erro ao enviar chats selecionados. ${response.error}`)
  }

  return (
    <Background>
      <Wrapper style={{ width: 'fit-content', height: 'fit-content' }}>
        <Header>
          <StyledTitle>Selecionar lista para adicionar chats</StyledTitle>
          <Close
            leftIcon={FiX}
            onClick={onClose}
            iconColor={THEME.colors.title}
            iconSize={24}
            variant="ghost"
          />
        </Header>
        <Input
          height="3.225rem"
          leftIcon={FiSearch}
          onChange={(e) => setSearch(e.target.value)}
          value={search}
          placeholder="Pesquisar lista"
        />
        <Column>
          {isLoading ? (
            <LoaderContainer>
              <Spinner />
            </LoaderContainer>
          ) : (
            data?.content.map((list: List) => (
              <CardList
                onClickOptions={() => {}}
                onSelected={() => handleAddChatsToList(list.id)}
                key={list.id}
                title={list.title}
              />
            ))
          )}
        </Column>
      </Wrapper>
    </Background>
  )
}
