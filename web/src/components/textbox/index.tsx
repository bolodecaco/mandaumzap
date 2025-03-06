import { uploadImageToMinio } from '@/app/actions/bucket/uploadImageToBucket'
import { Toolbar } from '../toolbar'
import {
  Container,
  TextArea,
  ImagePreview,
  ImageContainer,
  RemoveButton,
} from './styles'
import { ComponentProps, useRef } from 'react'

interface TextBoxProps extends ComponentProps<'textarea'> {
  value: string
  onEdit: (text: string) => void
  uploadedImage: string | undefined
  onImageUpload: (imageUrl: string | undefined) => void
}

export const TextBox = ({
  value,
  onEdit,
  uploadedImage,
  onImageUpload,
  ...rest
}: TextBoxProps) => {
  const textAreaRef = useRef<HTMLTextAreaElement | null>(null)

  const applyFormatting = (format: string) => {
    if (!textAreaRef.current) return
    const start = textAreaRef.current.selectionStart
    const end = textAreaRef.current.selectionEnd
    const text = value
    const selectedText = text.substring(start, end)
    let formattedText = text

    if (format === 'bold')
      formattedText =
        text.substring(0, start) + `*${selectedText}*` + text.substring(end)
    else if (format === 'italic')
      formattedText =
        text.substring(0, start) + `_${selectedText}_` + text.substring(end)
    else if (format === 'strike')
      formattedText =
        text.substring(0, start) + `~${selectedText}~` + text.substring(end)
    else if (format === 'code')
      formattedText =
        text.substring(0, start) +
        '`' +
        selectedText +
        '`' +
        text.substring(end)
    else if (format === 'quote')
      formattedText =
        text.substring(0, start) + `> ${selectedText}` + text.substring(end)

    onEdit(formattedText)
  }

  const handleImageUpload = async (file: File) => {
    const imageUrl = await uploadImageToMinio(file)
    if (imageUrl) onImageUpload(imageUrl) // Only one image at a time
  }

  const removeImage = () => onImageUpload(undefined) // Clear image

  return (
    <Container>
      <TextArea {...rest} value={value} ref={textAreaRef} />

      <Toolbar
        onApplyFormatting={applyFormatting}
        onUploadImage={handleImageUpload}
      />

      {uploadedImage && (
        <ImageContainer>
          <ImagePreview>
            <img src={uploadedImage} alt="Uploaded preview" />
            <RemoveButton onClick={removeImage}>X</RemoveButton>
          </ImagePreview>
        </ImageContainer>
      )}
    </Container>
  )
}
