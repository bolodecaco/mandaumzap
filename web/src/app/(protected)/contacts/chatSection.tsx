'use client'

import { CardContact } from '@/components/cardContact'
import { Checkbox } from '@/components/cardContact/styles'
import { Empty } from '@/components/empty'
import { Input } from '@/components/input'
import { Selector } from '@/components/selector'
import { Row, Title, Wrapper } from '@/lib/styled/global'
import { useRouter } from 'next/navigation'
import { useCallback, useMemo, useState } from 'react'
import { FiSearch } from 'react-icons/fi'
import { MdGroupAdd } from 'react-icons/md'
import { ListHeader, ListName, Phone, UserDiv } from './styles'

export const ChatsSection = () => {
  const router = useRouter()
  const [checkAll, setCheckAll] = useState(false)
  const [openSelectorChats, setOpenSelectorChats] = useState<string | null>(
    null,
  )

  const handleOpenSelectorChats = (name: string) => {
    setOpenSelectorChats((prev) => (prev === name ? null : name))
  }

  const initialContacts = useMemo(
    () =>
      Array(4).fill({
        name: 'joao',
        phone: '99999-9999',
        list: 'Lista',
        checked: false,
      }),
    [],
  )

  const [dataContact, setDataContact] = useState(initialContacts)

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
          options={['Mais recente', 'Mais antigo']}
          onSelect={() => {}}
          height="2.5rem"
          isOpen={openSelectorChats === 'ordenar'}
          onOpenChange={() => handleOpenSelectorChats('ordenar')}
        />
        <Selector
          label="Filtrar"
          options={['Disponível', 'Indisponível']}
          onSelect={() => {}}
          height="2.5rem"
          isOpen={openSelectorChats === 'filtrar'}
          onOpenChange={() => handleOpenSelectorChats('filtrar')}
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

      {dataContact.length === 0 ? (
        <Empty
          message="Não há chats sincronizados"
          icon={MdGroupAdd}
          action="Tente reconectar a sessão"
          onActionClick={() => {
            handleReconnect()
          }}
        />
      ) : (
        dataContact.map((data, index) => (
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
