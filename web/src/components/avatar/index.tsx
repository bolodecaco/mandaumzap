'use client'

import Image from 'next/image'
import { Wrapper } from './styles'

interface AvatarProps {
  imageURL?: string | null
}

export const Avatar = ({ imageURL }: AvatarProps) => (
  <Wrapper>
    <Image
      src={imageURL || '/assets/images/default-user.svg'}
      alt="Imagem de perfil do usuário"
      fill
    />
  </Wrapper>
)
