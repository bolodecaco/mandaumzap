'use client'

import { Column, Wrapper } from '@/lib/styled/global'
import { Background, Close, Header, StyledTitle } from '../styles'
import { THEME } from '@/lib/styled/theme'
import { FiX } from 'react-icons/fi'
import { useState } from 'react'
import { Label } from '@/components/label'
import { Input } from '@/components/input'
import { Button } from '@/components/button'
import { createList } from '@/app/actions/lists/createList'
import { toast } from 'react-toastify'
import { useQueryClient } from '@tanstack/react-query'

interface NewListModalProps {
  onClose: () => void
}

export const NewListModal = ({ onClose }: NewListModalProps) => {
  const [listName, setListName] = useState('')
  const queryClient = useQueryClient()

  const handleSubmit = async () => {
    const response = await createList({ listName })

    if (response.success) {
      toast.success('Lista criada com sucesso', { toastId: 'listSuccess' })
      queryClient.refetchQueries({
        queryKey: ['lists'],
      })
      onClose()
      return
    }

    toast.error(`Erro ao criar lista. ${response.error}`, {
      toastId: 'listError',
    })
  }

  return (
    <Background>
      <Wrapper style={{ width: 'fit-content', height: 'fit-content' }}>
        <Header>
          <StyledTitle>Criar nova lista</StyledTitle>
          <Close
            leftIcon={FiX}
            onClick={onClose}
            iconColor={THEME.colors.title}
            iconSize={24}
            variant="ghost"
          />
        </Header>

        <Column style={{ gap: '0.5rem' }}>
          <Label htmlFor="listName">
            Nome<strong style={{ color: 'red' }}>*</strong>
          </Label>
          <Input
            name="listName"
            id="listName"
            value={listName}
            onChange={(e) => setListName(e.target.value)}
            placeholder="Insira o nome da lista"
          />
          <Button
            text="Concluir"
            disabled={listName.trim() === ''}
            onClick={handleSubmit}
          />
        </Column>
      </Wrapper>
    </Background>
  )
}
