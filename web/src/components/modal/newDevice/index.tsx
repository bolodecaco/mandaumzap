import { useQRCodeTimer } from '@/components/modal/newDevice/useQRCodeTimer'
import { ProgressBar } from '@/components/progressBar'
import { Main } from '@/lib/styled/global'
import { THEME } from '@/lib/styled/theme'
import { useEffect, useState } from 'react'
import { FiX } from 'react-icons/fi'
import {
  Background,
  Close,
  Header,
  StyledTitle,
  StyledWrapper,
} from '../styles'
import { QRCodeDisplay } from './QRCodeDisplay'
import { Cancel, Description, ProgressContainer } from './styles'
import { useQueryClient } from '@tanstack/react-query'
import { toast } from 'react-toastify'
import { useCreateSession } from '@/services/session/useCreateSession'

export const NewDevice = ({ onClose }: { onClose: () => void }) => {
  const [qrCode, setQrCode] = useState<string | null>(null)
  const queryClient = useQueryClient()

  const { data, isLoading, error, refetch } = useCreateSession()

  useEffect(() => {
    setQrCode(data?.qrcode ?? null)
    queryClient.invalidateQueries({
      queryKey: ['sessions'],
      refetchType: 'active',
    })
  }, [data, queryClient])

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
    toast.error(`Erro ao gerar QRCode: ${error}`, { toastId: 'qrcode' })
  }

  return (
    <Background>
      <StyledWrapper style={{ width: '27.5rem' }}>
        <Header>
          <StyledTitle>Conectar novo dispositivo</StyledTitle>
          <Close
            leftIcon={FiX}
            onClick={onClose}
            iconColor={THEME.colors.title}
            iconSize={24}
            variant="ghost"
          />
        </Header>
        <Main style={{ justifyContent: 'center', alignItems: 'center' }}>
          <QRCodeDisplay
            isLoading={isLoading}
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
