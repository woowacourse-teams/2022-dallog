/* eslint-disable react/display-name */
import { AxiosError } from 'axios';
import { Component, ReactNode } from 'react';

import useSnackBar from '@/hooks/useSnackBar';

import { ERROR_MESSAGE } from '@/constants/message';

interface Props {
  children?: ReactNode;
  openSnackBar: (text: string) => void;
}

interface State {
  hasError: boolean;
}

export const withHooksHOC = (Component: any) => {
  return (props: any) => {
    const { openSnackBar } = useSnackBar();

    return <Component openSnackBar={openSnackBar} {...props} />;
  };
};

class ErrorBoundary extends Component<Props, State> {
  public state: State = {
    hasError: false,
  };

  public static getDerivedStateFromError(): State {
    return { hasError: true };
  }

  public componentDidCatch(error: unknown) {
    if (error instanceof AxiosError) {
      this.props.openSnackBar(error.response?.data.message ?? ERROR_MESSAGE.DEFAULT);
      return;
    }

    this.props.openSnackBar(ERROR_MESSAGE.DEFAULT);
  }

  public render() {
    return this.props.children;
  }
}

export default withHooksHOC(ErrorBoundary);
