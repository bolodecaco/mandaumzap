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
import { Selector } from '@/components/selector'
import { useDebounce } from '@/hooks/useDebouce'

const ORDER_OPTIONS = [
  {
    id: 1,
    value: 'lastActiveAt',
    name: 'Ativo recentemente',
  },
  {
    id: 2,
    value: 'title',
    name: 'Nome (A-z)',
  },
  {
    id: 3,
    value: '-title',
    name: 'Nome (Z-a)',
  },
]

const Lists = () => {
  const [search, setSearch] = useState('')
  const debouncedSearch = useDebounce(search, 300)
  const [sort, setSort] = useState<'title' | '-title' | 'lastActiveAt' | ''>('')
  const [isNewListModalOpen, setIsNewListModalOpen] = useState(false)
  const { data, error, isLoading } = useGetLists({
    search: debouncedSearch,
    sort,
  })

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
        <Selector
          label="Ordenar"
          options={ORDER_OPTIONS}
          value={sort || ''}
          onValueChange={(newValue) =>
            setSort(newValue as 'title' | '-title' | 'lastActiveAt' | '')
          }
          height="2.5rem"
        />
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
