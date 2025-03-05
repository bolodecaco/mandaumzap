'use client'

import { Button } from '@/components/button'
import { Input } from '@/components/input'
import { Label } from '@/components/label'
import { TextBox } from '@/components/textbox'
import { Column, Row, Title, Wrapper } from '@/lib/styled/global'
import { useMemo, useState } from 'react'
import { AddList, Clear } from './styles'
import { RxFilePlus } from 'react-icons/rx'
import { THEME } from '@/lib/styled/theme'
import { useGetSessions } from '@/services/session/useGetSessions'
import { SendToListModal } from '@/components/modal/sendToList'
import { List } from '@/@types/list'
import { Selector } from '@/components/selector'
import { createMessage } from '@/app/actions/messages/createMessage'
import { toast } from 'react-toastify'
import { sendMessage } from '@/app/actions/messages/sendMessage'
import { BiX } from 'react-icons/bi'

export function Content() {
  const [message, setMessage] = useState('')
  const [uploadedImage, setUploadedImage] = useState<string | undefined>(
    undefined,
  )
  const [isListsModalOpen, setIsListsModalOpen] = useState(false)
  const [receiverList, setReceiverList] = useState({} as List)
  const [sessionId, setSessionId] = useState('')
  const [isSendingMessage, setIsSendingMessage] = useState(false)

  const { data: sessions, isLoading } = useGetSessions()

  const SESSION_OPTIONS = useMemo(
    () =>
      sessions
        ?.filter((session) => session.status === 'open')
        .map((session) => ({
          id: session.id,
          value: session.id,
          name: session.id,
        })),
    [sessions],
  )

  const handleResetFields = () => {
    setMessage('')
    setUploadedImage(undefined)
    setReceiverList({} as List)
    setSessionId('')
  }

  const handleSelectList = (list: List) => {
    setReceiverList(list)
    setIsListsModalOpen(false)
  }

  const handleImageUpload = (imageUrl: string | undefined) => {
    setUploadedImage(imageUrl)
  }

  const handleSessionChange = (newValue: string) => {
    setSessionId(newValue)
  }

  const handleSendMessage = async () => {
    if (!message || !sessionId || !receiverList)
      toast.error('É preciso preencher todos os campos')

    setIsSendingMessage(true)

    const response = await createMessage({
      content: message,
      listId: receiverList.id,
      sessionId,
      url: uploadedImage,
    })

    if (response.success) {
      const res = await sendMessage({ messageId: response.value.id })
      if (res.success) {
        toast.success('Iniciando o envio da mensagem.', {
          toastId: 'messageSuccess',
        })
        setIsSendingMessage(false)
      }
      return
    }

    setIsSendingMessage(false)

    toast.error(`Erro ao enviar mensagem. ${response.error}`, {
      toastId: 'messageError',
    })
  }

  return (
    <>
      <Row style={{ flex: 1 }}>
        <Wrapper style={{ flex: 3, overflow: 'auto' }}>
          <Title>Enviar mensagem</Title>

          <Column style={{ gap: '0.5rem' }}>
            <Label>
              Sessão<strong style={{ color: 'red' }}>*</strong>
            </Label>
            <div>
              <Selector
                label="Sessão"
                isLoading={isLoading}
                options={SESSION_OPTIONS || []}
                value={sessionId}
                onValueChange={handleSessionChange}
                height="2.5rem"
              />
            </div>
            <Label htmlFor="receiver">
              Destinatário<strong style={{ color: 'red' }}>*</strong>
            </Label>
            {!receiverList.id ? (
              <AddList
                leftIcon={RxFilePlus}
                iconColor={THEME.colors.title}
                iconSize={20}
                variant="ghost"
                onClick={() => setIsListsModalOpen(true)}
              />
            ) : (
              <AddList
                text={receiverList.title}
                variant="ghost"
                style={{
                  width: 'fit-content',
                  paddingInline: '1rem',
                  fontWeight: '500',
                }}
                rightIcon={BiX}
                iconColor={THEME.colors.title}
                iconSize={16}
                onClick={() => setReceiverList({} as List)}
              />
            )}
          </Column>

          <Column style={{ gap: '0.5rem', flex: 1 }}>
            <Label htmlFor="message">
              Mensagem<strong style={{ color: 'red' }}>*</strong>
            </Label>
            <TextBox
              placeholder="Digite uma mensagem para ser enviado à todos da lista"
              value={message}
              onChange={(e) => setMessage(e.target.value)}
              onEdit={setMessage}
              onImageUpload={handleImageUpload}
              uploadedImage={uploadedImage}
              id="message"
            />
          </Column>

          <Row style={{ gap: '0.5rem', justifyContent: 'flex-end' }}>
            <Clear
              text="Limpar tudo"
              variant="ghost"
              onClick={handleResetFields}
            />
            <Button
              text={isSendingMessage ? 'Enviando' : 'Enviar'}
              style={{ width: '6.625rem' }}
              isLoading={isSendingMessage}
              onClick={handleSendMessage}
            />
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

      {isListsModalOpen && (
        <SendToListModal
          onClose={() => setIsListsModalOpen(false)}
          onListSelected={handleSelectList}
        />
      )}
    </>
  )
}
