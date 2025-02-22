import { ComponentProps } from 'react'
import { Container } from './styles'

export const Label = ({ ...rest }: ComponentProps<'label'>) => {
  return <Container {...rest}>{rest.children}</Container>
}
