'use client'

import { ButtonGroup, Container, Divider } from './styles'
import { Option } from './option'
import { useRef } from 'react'

interface ToolbarProps {
  onApplyFormatting: (format: string) => void
  onUploadImage: (file: File) => void
}

export const Toolbar = ({ onApplyFormatting, onUploadImage }: ToolbarProps) => {
  const fileInputRef = useRef<HTMLInputElement>(null)

  const handleImageSelect = () => {
    fileInputRef.current?.click()
  }

  return (
    <Container>
      <ButtonGroup>
        <Option
          onClick={() => onApplyFormatting('bold')}
          iconName="bold"
          iconAlt="ícone de negrito"
        />
        <Option
          onClick={() => onApplyFormatting('italic')}
          iconName="italic"
          iconAlt="ícone de itálico"
        />
        <Option
          onClick={() => onApplyFormatting('strike')}
          iconName="strike"
          iconAlt="ícone de tachado"
        />
      </ButtonGroup>

      <Divider />

      <ButtonGroup>
        <Option
          onClick={() => onApplyFormatting('quote')}
          iconName="quote"
          iconAlt="ícone de citação"
        />
        <Option
          onClick={() => onApplyFormatting('code')}
          iconName="code"
          iconAlt="ícone de trecho de código"
        />
      </ButtonGroup>

      <Divider />

      <ButtonGroup>
        <Option
          //   onClick={() => onApplyFormatting('emoji')}
          iconName="emoji"
          iconAlt="ícone de emoji"
        />
        <ButtonGroup>
          <Option
            onClick={handleImageSelect}
            iconName="image"
            iconAlt="ícone de imagem"
          />
          <input
            type="file"
            accept="image/*"
            ref={fileInputRef}
            style={{ display: 'none' }}
            onChange={(e) => {
              if (e.target.files?.[0]) {
                onUploadImage(e.target.files[0])
              }
            }}
          />
        </ButtonGroup>
      </ButtonGroup>
    </Container>
  )
}
