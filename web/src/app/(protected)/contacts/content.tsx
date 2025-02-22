'use client'

import { Button } from '@/components/button'
import { Empty } from '@/components/empty'
import { Input } from '@/components/input'
import { Selector } from '@/components/selector'
import { Row, Title, Wrapper } from '@/lib/styled/global'
import { FiSearch } from 'react-icons/fi'
import { HiPlus } from 'react-icons/hi'
import { MdGroupAdd } from 'react-icons/md'
import { RiPlayListAddFill } from 'react-icons/ri'
import { List } from './styles'

export const Content = () => {
  const data = []

  return (
    <>
      <Row style={{ flex: 1, minHeight: 0 }}>
        <Wrapper style={{ flex: 3, overflow: 'auto' }}>
          <Title>Meus chats</Title>
          <Row style={{ gap: '0.5rem' }}>
            <Input
              keybind={'Ctrl + K'}
              width="22.125rem"
              height="2.5rem"
              leftIcon={FiSearch}
              placeholder="Pesquisar chat"
            />
            <Selector
              label="Ordenar"
              options={['Mais recente', 'Mais antigo']}
              onSelect={(opt) => console.log(opt)}
              height="2.5rem"
            />
            <Selector
              label="Filtrar"
              options={['Disponível', 'Indisponível']}
              onSelect={(opt) => console.log(opt)}
              height="2.5rem"
            />
          </Row>

          {data?.length === 0 ? (
            <Empty
              message={'Não há chats sincronizados'}
              icon={MdGroupAdd}
              action="Tente reconectar a sessão"
              onActionClick={() => {}}
            />
          ) : (
            <List></List>
          )}
        </Wrapper>

        <Wrapper
          style={{
            flex: 1,
            alignItems: 'center',
          }}
        >
          <Title style={{ width: '100%' }}>Minhas listas</Title>
          <Input
            keybind={'Ctrl + L'}
            height="3.225rem"
            leftIcon={FiSearch}
            placeholder="Pesquisar lista"
          />
          <Row style={{ gap: '0.5rem' }}>
            <Button text="Nova lista" leftIcon={HiPlus} onClick={() => {}} />
            <Selector
              label="Ordenar"
              options={['Mais recente', 'Mais antigo']}
              onSelect={(opt) => console.log(opt)}
              height="2.5rem"
            />
            <Selector
              label="Filtrar"
              options={['Disponível', 'Indisponível']}
              onSelect={(opt) => console.log(opt)}
              height="2.5rem"
            />
          </Row>

          {data?.length === 0 ? (
            <Empty
              message={'Nenhuma lista cadastrada'}
              icon={RiPlayListAddFill}
              action="Criar nova lista"
              onActionClick={() => {}}
            />
          ) : (
            <List></List>
          )}
        </Wrapper>
      </Row>
    </>
  )
}
