import { Toolbar } from '../toolbar'
import { Container, TextArea } from './styles'
import { ComponentProps, useRef } from 'react'

interface TextBoxProps extends ComponentProps<'textarea'> {
  value: string
  onEdit: (text: string) => void
}

export const TextBox = ({ value, onEdit, ...rest }: TextBoxProps) => {
  const textAreaRef = useRef<HTMLTextAreaElement | null>(null)

  const applyFormatting = (format: string) => {
    if (!textAreaRef.current) return
    const start = textAreaRef.current.selectionStart
    const end = textAreaRef.current.selectionEnd
    const text = value
    const selectedText = text.substring(start, end)
    let formattedText = text

    if (format === 'bold') {
      formattedText =
        text.substring(0, start) + `*${selectedText}*` + text.substring(end)
    } else if (format === 'italic') {
      formattedText =
        text.substring(0, start) + `_${selectedText}_` + text.substring(end)
    } else if (format === 'strike') {
      formattedText =
        text.substring(0, start) + `~${selectedText}~` + text.substring(end)
    } else if (format === 'code') {
      formattedText =
        text.substring(0, start) +
        '`' +
        selectedText +
        '`' +
        text.substring(end)
    } else if (format === 'quote') {
      formattedText =
        text.substring(0, start) + `> ${selectedText}` + text.substring(end)
    }

    onEdit(formattedText)
  }

  return (
    <Container>
      <TextArea {...rest} value={value} ref={textAreaRef} />
      <Toolbar onApplyFormatting={(format) => applyFormatting(format)} />
    </Container>
  )
}
