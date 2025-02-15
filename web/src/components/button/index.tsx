import { ComponentProps, ElementType } from 'react'
import { Container } from './styles'

interface ButtonProps extends ComponentProps<'button'> {
  leftIcon?: ElementType
  rightIcon?: ElementType
  iconColor?: string
  iconSize?: number
  text?: string
  variant?: 'primary' | 'ghost'
}

export const Button = ({
  leftIcon: LeftIcon,
  rightIcon: RightIcon,
  text,
  iconSize = 16,
  iconColor = 'white',
  variant = 'primary',
  ...props
}: ButtonProps) => {
  return (
    <Container $variant={variant} {...props}>
      {LeftIcon && <LeftIcon size={iconSize} color={iconColor} />}
      {text}
      {RightIcon && <RightIcon size={iconSize} color={iconColor} />}
    </Container>
  )
}
