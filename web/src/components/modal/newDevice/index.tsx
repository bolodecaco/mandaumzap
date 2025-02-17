import { Button } from '@/components/button'
import { useQRCodeTimer } from '@/components/modal/newDevice/useQRCodeTimer'
import { ProgressBar } from '@/components/progressBar'
import { Main } from '@/lib/styled/global'
import { THEME } from '@/lib/styled/theme'
import { useEffect, useState } from 'react'
import { FiX } from 'react-icons/fi'
import { Background, Header, StyledTitle, StyledWrapper } from '../styles'
import { QRCodeDisplay } from './QRCodeDisplay'
import { Cancel, Description, ProgressContainer } from './styles'
import { QueryClient, useQuery } from '@tanstack/react-query'
import { createSession } from '@/app/actions/sessions/createSession'
import { toast } from 'react-toastify'

export const NewDevice = ({ onClose }: { onClose: () => void }) => {
  const [qrCode, setQrCode] = useState<string | null>(null)

  const { data, isLoading, error, refetch, isRefetching } = useQuery({
    queryKey: ['session'],
    queryFn: () => createSession(),
    refetchOnMount: false,
    refetchOnWindowFocus: false,
  })

  useEffect(() => {
    if (data?.success) {
      const queryClient = new QueryClient()

      setQrCode(data.value.qrcode ?? null)
      queryClient.refetchQueries({
        queryKey: ['sessions'],
      })
    }
  }, [data])

  const { progress, remainingTime, resetTimer } = useQRCodeTimer({
    initialTime: 40,
    onExpire: () => setQrCode(null),
    qrCode,
  })

  const handleRefresh = () => {
    refetch()
    resetTimer()
  }

  if (error) {
    toast.error(`Erro ao gerar QRCode. Detalhes: ${error}`)
  }

  return (
    <Background>
      <StyledWrapper style={{ width: '27.5rem' }}>
        <Header>
          <StyledTitle>Conectar novo dispositivo</StyledTitle>
          <Button
            leftIcon={FiX}
            onClick={onClose}
            iconColor={THEME.colors.title}
            iconSize={24}
            variant="ghost"
          />
        </Header>
        <Main style={{ justifyContent: 'center', alignItems: 'center' }}>
          <QRCodeDisplay
            isLoading={isLoading || isRefetching}
            qrCode={qrCode}
            onRefresh={handleRefresh}
          />
          <Description>
            Escaneie esse QR Code para conectar seu WhatsApp
          </Description>
          <ProgressContainer>
            <ProgressBar progress={progress} />
            <Description>{Math.ceil(remainingTime)}s</Description>
          </ProgressContainer>
          <Cancel text="Cancelar" onClick={onClose} variant="ghost" />
        </Main>
      </StyledWrapper>
    </Background>
  )
}
