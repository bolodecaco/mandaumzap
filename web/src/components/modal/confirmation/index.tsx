import { Button } from '@/components/button'
import {
  Background,
  Close,
  Header,
  StyledTitle,
  StyledWrapper,
} from '../styles'
import { THEME } from '@/lib/styled/theme'
import { FiX } from 'react-icons/fi'
import { Row } from '@/lib/styled/global'
import { Cancel, Description } from './styles'
import { ReactNode } from 'react'

interface ConfirmationProps {
  title: string
  content: ReactNode
  confirmButtonText: string
  cancelButtonText: string
  onConfirmButtonClick: () => void
  onCancelButtonClick: () => void
  onCloseButtonClick: () => void
}

export const Confirmation = ({
  cancelButtonText,
  confirmButtonText,
  content,
  onCancelButtonClick,
  onCloseButtonClick,
  onConfirmButtonClick,
  title,
}: ConfirmationProps) => {
  return (
    <Background>
      <StyledWrapper style={{ width: '30rem' }}>
        <Header>
          <StyledTitle>{title}</StyledTitle>
          <Close
            leftIcon={FiX}
            onClick={onCloseButtonClick}
            iconColor={THEME.colors.title}
            iconSize={24}
            variant="ghost"
          />
        </Header>

        <Description>{content}</Description>

        <Row style={{ justifyContent: 'flex-end' }}>
          <Cancel
            onClick={onCancelButtonClick}
            variant="ghost"
            text={cancelButtonText}
          />
          <Button onClick={onConfirmButtonClick} text={confirmButtonText} />
        </Row>
      </StyledWrapper>
    </Background>
  )
}
