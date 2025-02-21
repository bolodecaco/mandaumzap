'use client'

import { TextBox } from '@/components/textbox'
import { Row, Title, Wrapper } from '@/lib/styled/global'
import { useState } from 'react'

export function Content() {
  const [message, setMessage] = useState('')

  return (
    <>
      <Row style={{ flex: 1 }}>
        <Wrapper style={{ flex: 3, overflow: 'auto' }}>
          <Title>Enviar mensagem</Title>
          <TextBox
            placeholder="Digite uma mensagem para ser enviado à todos da lista"
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            onEdit={(t) => setMessage(t)}
          />
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
