import { Button } from '@/components/button'
import { useQRCodeTimer } from '@/components/modal/newDevice/useQRCodeTimer'
import { ProgressBar } from '@/components/progressBar'
import { Main } from '@/lib/styled/global'
import { THEME } from '@/lib/styled/theme'
import { useState } from 'react'
import { FiX } from 'react-icons/fi'
import { Background, Header, StyledTitle, StyledWrapper } from '../styles'
import { QRCodeDisplay } from './QRCodeDisplay'
import { Cancel, Description, ProgressContainer } from './styles'

const INITIAL_QR_CODE =
  '2@7Qp73ki497pk15x7BULoZGk3h9tjSd09iRiMxxYKjwc5hbOWAsYmYA5HNdj/UvFmX6QV+xBSpOQ9lfhNZ1mc3YW4iyXmoiOLg0E=,iK+d7W5DhB+LKoMjG0JA6PoGepp8yQZemqC0bOMqEBg=,FAt2zdBORPv0LwMNQGy3Z020s+HzeaFjSXzFLpPyeTE=,/OzQYhF6DsULmKWD43ar+YkkT76qRnqT7u8FOVRHPk0='

export const NewDevice = ({ onClose }: { onClose: () => void }) => {
  const [qrCode, setQrCode] = useState<string | null>(null)

  const { progress, remainingTime, resetTimer } = useQRCodeTimer({
    initialTime: 40,
    onExpire: () => setQrCode(null),
    qrCode,
  })

  const handleRefresh = () => {
    setQrCode(INITIAL_QR_CODE)
    resetTimer()
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
          <QRCodeDisplay qrCode={qrCode} onRefresh={handleRefresh} />
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
