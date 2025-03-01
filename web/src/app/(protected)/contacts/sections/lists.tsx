'use client'

import { Button } from '@/components/button'
import { CardList } from '@/components/cardList'
import { Empty } from '@/components/empty'
import { Input } from '@/components/input'
import { Row, Title, Wrapper } from '@/lib/styled/global'
import { useGetLists } from '@/services/list/useGetLists'
import { useEffect } from 'react'
import { FiSearch } from 'react-icons/fi'
import { HiPlus } from 'react-icons/hi'
import { RiPlayListAddFill } from 'react-icons/ri'
import { toast } from 'react-toastify'
import { LoaderContainer, Spinner } from './styles'

const Lists = () => {
  const { data, error, isLoading } = useGetLists()

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
      />
      <Row style={{ gap: '0.5rem' }}>
        <Button text="Nova lista" leftIcon={HiPlus} onClick={() => {}} />
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
      ) : data?.length === 0 ? (
        <Empty
          message="Nenhuma lista cadastrada"
          icon={RiPlayListAddFill}
          action="Criar nova lista"
          onActionClick={() => {}}
        />
      ) : (
        data?.map((list) => (
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
    </Wrapper>
  )
}

export default Lists
