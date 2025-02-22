import { ComponentProps, ElementType } from 'react'
import { Container, Keybind, StyledInput } from './styles'

interface InputProps extends ComponentProps<'input'> {
  leftIcon?: ElementType
  keybind?: string
  width?: string
  height?: string
}

export const Input = ({
  leftIcon: LeftIcon,
  keybind,
  width,
  height,
  ...rest
}: InputProps) => {
  return (
    <Container $width={width} $height={height}>
      {LeftIcon && <LeftIcon size={24} color="#878787" />}
      <StyledInput {...rest} />
      {keybind && <Keybind>{keybind}</Keybind>}
    </Container>
  )
}
