import { Button } from '@/components/button'
import { CardList } from '@/components/cardList'
import { Empty } from '@/components/empty'
import { Input } from '@/components/input'
import { Selector } from '@/components/selector'
import { Row, Title, Wrapper } from '@/lib/styled/global'
import { useMemo } from 'react'
import { FiSearch } from 'react-icons/fi'
import { HiPlus } from 'react-icons/hi'
import { RiPlayListAddFill } from 'react-icons/ri'

interface ListSectionProps {
  openSelector: string | null
  handleOpenSelector: (name: string) => void
}

export const ListSection = ({
  openSelector,
  handleOpenSelector,
}: ListSectionProps) => {
  const data = useMemo(
    () =>
      Array(2).fill({
        title: 'ADS 6˚ periodo',
        avatars: [
          { src: 'https://avatars.githubusercontent.com/u/101940943?v=4' },
        ],
      }),
    [],
  )

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
        <Selector
          label="Ordenar"
          options={['Mais recente', 'Mais antigo']}
          onSelect={() => {}}
          height="2.5rem"
          isOpen={openSelector === 'ordenar'}
          onOpenChange={() => handleOpenSelector('ordenar')}
        />
        <Selector
          label="Filtrar"
          options={['Disponível', 'Indisponível']}
          onSelect={() => {}}
          height="2.5rem"
          isOpen={openSelector === 'filtrar'}
          onOpenChange={() => handleOpenSelector('filtrar')}
        />
      </Row>

      {data.length === 0 ? (
        <Empty
          message="Nenhuma lista cadastrada"
          icon={RiPlayListAddFill}
          action="Criar nova lista"
          onActionClick={() => {}}
        />
      ) : (
        data.map((item, index) => (
          <CardList
            key={`${item.title}-${index}`}
            {...item}
            lastUpdate="Último envio às 11:27"
            onClickOptions={() => {}}
          />
        ))
      )}
    </Wrapper>
  )
}
