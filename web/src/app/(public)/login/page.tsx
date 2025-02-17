'use client'

import { Button } from '@/components/button'
import { signIn } from 'next-auth/react'
import Image from 'next/image'
import {
  Callout,
  Footer,
  ImageContainer,
  StyledPage,
  Subtitle,
  Title,
} from './styles'

export default function SignIn() {
  return (
    <StyledPage>
      <Callout>
        <div>
          <Title>mandaumzap</Title>
          <Subtitle>
            Conecte seu número, envie mensagens para vários contatos ao mesmo
            tempo e crie rotinas automáticas para otimizar sua comunicação! 📲✨
          </Subtitle>
        </div>

        <Button
          text="Entrar agora"
          onClick={() => signIn('keycloak')}
          style={{ width: '12.25rem' }}
        />
      </Callout>

      <ImageContainer>
        <Image
          src="/assets/images/preview.png"
          fill
          objectFit="contain"
          objectPosition="right"
          alt="Imagem de preview do dashboard do mandaumzap"
        />
      </ImageContainer>

      <Footer>Copyright © 2025 mandaumzap</Footer>
    </StyledPage>
  )
}
