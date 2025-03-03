'use client'

import { List } from '@/@types/list'
import { Button } from '@/components/button'
import { CardList } from '@/components/cardList'
import { Empty } from '@/components/empty'
import { Input } from '@/components/input'
import { Row, Title, Wrapper } from '@/lib/styled/global'
import { useGetLists } from '@/services/list/useGetLists'
import { useEffect, useState } from 'react'
import { FiSearch } from 'react-icons/fi'
import { HiPlus } from 'react-icons/hi'
import { RiPlayListAddFill } from 'react-icons/ri'
import { toast } from 'react-toastify'
import { LoaderContainer, Spinner } from './styles'
import { NewListModal } from '@/components/modal/newList'

const Lists = () => {
  const [search, setSearch] = useState('')
  const [isNewListModalOpen, setIsNewListModalOpen] = useState(false)
  const { data, error, isLoading } = useGetLists({ search })

  useEffect(() => {
    if (error) {
      toast.error(`Erro ao carregar listas. Tente carregar a página.`, {
        toastId: 'lists',
      })
    }
  }, [error])

  return (
    <Wrapper style={{ flex: 1, alignItems: 'center' }}>
      <Title style={{ width: '100%' }}>Minhas listas</Title>
      <Input
        keybind="Ctrl + L"
        height="3.225rem"
        leftIcon={FiSearch}
        placeholder="Pesquisar lista"
        value={search}
        onChange={(e) => setSearch(e.target.value)}
      />
      <Row style={{ gap: '0.5rem' }}>
        <Button
          text="Nova lista"
          leftIcon={HiPlus}
          onClick={() => setIsNewListModalOpen(true)}
        />
        {/* <Selector
          label="Ordenar"
          options={['Mais recente', 'Mais antigo']}
          onSelect={() => {}}
          height="2.5rem"
          isOpen={openSelectorLists === 'ordenar'}
          onOpenChange={() => handleOpenSelectorLists('ordenar')}
        />
        <Selector
          label="Filtrar"
          options={['Disponível', 'Indisponível']}
          onSelect={() => {}}
          height="2.5rem"
          isOpen={openSelectorLists === 'filtrar'}
          onOpenChange={() => handleOpenSelectorLists('filtrar')}
        /> */}
      </Row>

      {isLoading ? (
        <LoaderContainer>
          <Spinner />
        </LoaderContainer>
      ) : data?.content.length === 0 ? (
        <Empty
          message="Nenhuma lista cadastrada"
          icon={RiPlayListAddFill}
          action="Criar nova lista"
          onActionClick={() => {}}
        />
      ) : (
        data?.content.map((list: List) => (
          <CardList
            key={list.id}
            title={list.title}
            lastUpdate={
              list.lastActiveAt
                ? list.lastActiveAt.toISOString()
                : 'Nenhuma mensagem enviada'
            }
            onClickOptions={() => {}}
          />
        ))
      )}

      {isNewListModalOpen && (
        <NewListModal onClose={() => setIsNewListModalOpen(false)} />
      )}
    </Wrapper>
  )
}

export default Lists
