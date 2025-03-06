'use client'

import { Row } from '@/lib/styled/global'
import { Title, Wrapper } from './styles'
import { useSession } from 'next-auth/react'
import { Avatar } from '../avatar'
import Skeleton from 'react-loading-skeleton'

interface HeaderProps {
  pageTitle: string
}

export const Header = ({ pageTitle }: HeaderProps) => {
  const { data: session, status } = useSession()

  return (
    <Wrapper style={{ justifyContent: 'space-between' }}>
      <Title>{pageTitle}</Title>
      <Row
        style={{ width: 'fit-content', alignItems: 'center', gap: '0.5rem' }}
      >
        {status === 'loading' ? (
          <>
            <Skeleton height={16} width={100} />
            <Skeleton borderRadius={9999} width={32} height={32} />
          </>
        ) : (
          <>
            <Title style={{ fontWeight: 500 }}>{session?.user?.name}</Title>
            <Avatar imageURL={session?.user?.image} />
          </>
        )}
      </Row>
    </Wrapper>
  )
}
