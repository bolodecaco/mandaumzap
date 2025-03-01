'use client'

import { Button } from '@/components/button'
import { Input } from '@/components/input'
import { Label } from '@/components/label'
import { TextBox } from '@/components/textbox'
import { Column, Row, Title, Wrapper } from '@/lib/styled/global'
import { THEME } from '@/lib/styled/theme'
import { useState } from 'react'
import { RxFilePlus } from 'react-icons/rx'
import { AddList, Clear } from './styles'

export function Content() {
  const [message, setMessage] = useState('')

  return (
    <>
      <Row style={{ flex: 1 }}>
        <Wrapper style={{ flex: 3, overflow: 'auto' }}>
          <Title>Enviar mensagem</Title>

          <Column style={{ gap: '0.5rem' }}>
            <Label htmlFor="receiver">Destinatário</Label>
            <Row style={{ gap: '0.25rem' }}>
              <AddList
                leftIcon={RxFilePlus}
                iconColor={THEME.colors.title}
                iconSize={20}
                variant="ghost"
              />
              <Input
                id="receiver"
                type="text"
                placeholder="Pesquise listas para enviar a mensagem"
                width="26rem"
              />
            </Row>
          </Column>

          <Column style={{ gap: '0.5rem', flex: 1 }}>
            <Label htmlFor="message">Mensagem</Label>
            <TextBox
              placeholder="Digite uma mensagem para ser enviado à todos da lista"
              value={message}
              onChange={(e) => setMessage(e.target.value)}
              onEdit={(t) => setMessage(t)}
              id="message"
            />
          </Column>

          <Row style={{ gap: '0.5rem', justifyContent: 'flex-end' }}>
            <Clear text="Limpar tudo" variant="ghost" />
            <Button text="Enviar" style={{ width: '6.625rem' }} weight="bold" />
          </Row>
        </Wrapper>
        <Wrapper
          style={{
            flex: 1,
            overflow: 'hidden',
          }}
        >
          <Title>Notificações</Title>
        </Wrapper>
      </Row>

      <Row style={{ flex: 1 }}>
        <Wrapper style={{ flex: 3, overflow: 'auto' }}>
          <Title>Rotinas mais usadas</Title>
        </Wrapper>
        <Wrapper
          style={{
            flex: 1,
            overflow: 'hidden',
          }}
        >
          <Title>Listas frequentes</Title>
        </Wrapper>
      </Row>
    </>
  )
}
