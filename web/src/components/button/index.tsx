'use client'

import { ComponentProps, ElementType } from 'react'
import { Container, Spinner } from './styles'

interface ButtonProps extends ComponentProps<'button'> {
  leftIcon?: ElementType
  rightIcon?: ElementType
  iconColor?: string
  iconSize?: number
  text?: string
  weight?: 'bold' | 'normal'
  variant?: 'primary' | 'ghost'
  isLoading?: boolean
}

export const Button = ({
  leftIcon: LeftIcon,
  rightIcon: RightIcon,
  text,
  isLoading,
  iconSize = 16,
  iconColor = 'white',
  variant = 'primary',
  ...props
}: ButtonProps) => {
  return (
    <Container
      $variant={variant}
      {...props}
      disabled={isLoading || props.disabled}
    >
      {!isLoading && LeftIcon && <LeftIcon size={iconSize} color={iconColor} />}
      {isLoading && <Spinner />}
      {isLoading ? 'Carregando...' : text}
      {!isLoading && RightIcon && (
        <RightIcon size={iconSize} color={iconColor} />
      )}
    </Container>
  )
}
