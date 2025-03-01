'use client'

import { Button } from '@/components/button'
import { Input } from '@/components/input'
import { Selector } from '@/components/selector'
import { Row, Title, Wrapper } from '@/lib/styled/global'
import { THEME } from '@/lib/styled/theme'
import { FiSearch } from 'react-icons/fi'
import { HiPlus } from 'react-icons/hi'
import { MdInfo } from 'react-icons/md'
import { DivInfo, Text } from './styles'

export const Content = () => {
  return (
    <>
      <Row style={{ flex: 1, minHeight: 0 }}>
        <Wrapper style={{ flex: 3, overflow: 'auto' }}>
          <Title>Rotinas</Title>
          <Row style={{ gap: '0.5rem' }}>
            <Input
              keybind="Ctrl + K"
              width="22.125rem"
              height="2.5rem"
              leftIcon={FiSearch}
              placeholder="Pesquisar chat"
              value={''}
              onChange={() => {}}
            />
            <Selector
              label="Ordenar"
              isLoading={false}
              options={[]}
              value={''}
              onValueChange={() => {}}
              height="2.5rem"
            />
            <Selector
              label="Filtrar"
              isLoading={false}
              options={[]}
              value={''}
              onValueChange={() => {}}
              height="2.5rem"
            />
            <Button
              text="Nova rotina"
              leftIcon={HiPlus}
              onClick={() => {}}
              weight="normal"
            />
            <DivInfo>
              <Text>6/6 rotinas</Text>
              <MdInfo color={THEME.colors.secondary} />
            </DivInfo>
          </Row>
        </Wrapper>
      </Row>
    </>
  )
}
