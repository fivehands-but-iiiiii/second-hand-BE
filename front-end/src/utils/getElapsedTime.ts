const getElapsedTime = (createdAt: string) => {
  const currentTime = new Date();
  const timeDifference =
    (currentTime.getTime() - new Date(createdAt).getTime()) / 1000;
  const timeOptions = {
    seconds: {
      value: 1,
      unit: '초 전',
    },
    minutes: {
      value: 60,
      unit: '분 전',
    },
    hours: {
      value: 60 * 60,
      unit: '시간 전',
    },
    days: {
      value: 60 * 60 * 24,
      unit: '일 전',
    },
    weeks: {
      value: 60 * 60 * 24 * 7,
      unit: '주 전',
    },
    months: {
      value: 60 * 60 * 24 * 7 * 4,
      unit: '달 전',
    },
    years: {
      value: 60 * 60 * 24 * 7 * 4 * 12,
      unit: '년 전',
    },
  };

  const formatElapsedTime = (unit: keyof typeof timeOptions): string => {
    return `${Math.floor(timeDifference / timeOptions[unit].value)}${
      timeOptions[unit].unit
    }`;
  };

  switch (true) {
    case timeDifference < timeOptions.minutes.value:
      return formatElapsedTime('seconds');
    case timeDifference < timeOptions.hours.value:
      return formatElapsedTime('minutes');
    case timeDifference < timeOptions.days.value:
      return formatElapsedTime('hours');
    case timeDifference < timeOptions.weeks.value:
      return formatElapsedTime('days');
    case timeDifference < timeOptions.months.value:
      return formatElapsedTime('weeks');
    case timeDifference < timeOptions.years.value:
      return formatElapsedTime('months');
    default:
      return formatElapsedTime('years');
  }
};

export default getElapsedTime;
