'use client'

import { Button } from '@/components/button'
import { Input } from '@/components/input'
import RoutineCard from '@/components/routineCard'
import { Selector } from '@/components/selector'
import { Row, Title, Wrapper } from '@/lib/styled/global'
import { THEME } from '@/lib/styled/theme'
import { useState } from 'react'
import { FiChevronDown, FiChevronUp, FiSearch } from 'react-icons/fi'
import { HiPlus } from 'react-icons/hi'
import { MdInfo } from 'react-icons/md'
import { CardGrid, DivInfo, Section, SectionHeader, Text } from './styles'

export const Content = () => {
  const [activeExpanded, setActiveExpanded] = useState(true)
  const [inactiveExpanded, setInactiveExpanded] = useState(true)

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

          <Section>
            <SectionHeader onClick={() => setActiveExpanded(!activeExpanded)}>
              (6) Ativas{' '}
              {activeExpanded ? (
                <FiChevronUp size={16} />
              ) : (
                <FiChevronDown size={16} />
              )}
            </SectionHeader>
            <CardGrid $expanded={activeExpanded}>
              <RoutineCard
                active
                createdAt="20/04/2000"
                days="Todos os dias"
                receivers="Lucas, vitor e renan"
                time="10:00"
                title="Envio de mensagens"
              />
              <RoutineCard
                active
                createdAt="20/04/2000"
                days="Todos os dias"
                receivers="Lucas, vitor e renan"
                time="10:00"
                title="Envio de mensagens"
              />
            </CardGrid>
          </Section>

          <Section>
            <SectionHeader
              onClick={() => setInactiveExpanded(!inactiveExpanded)}
            >
              (12) Inativas{' '}
              {inactiveExpanded ? (
                <FiChevronUp size={16} />
              ) : (
                <FiChevronDown size={16} />
              )}
            </SectionHeader>
            <CardGrid $expanded={inactiveExpanded}>
              <RoutineCard
                createdAt="20/04/2000"
                days="Todos os dias"
                receivers="Lucas, vitor e renan"
                time="10:00"
                title="Envio de mensagens"
              />
              <RoutineCard
                createdAt="20/04/2000"
                days="Todos os dias"
                receivers="Lucas, vitor e renan"
                time="10:00"
                title="Envio de mensagens"
              />
            </CardGrid>
          </Section>
        </Wrapper>
      </Row>
    </>
  )
}
