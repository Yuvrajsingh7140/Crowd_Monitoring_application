
import React from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import LinearProgress from '@mui/material/LinearProgress';

const statusColors = {
  SAFE: 'success',
  CAUTION: 'warning',
  WARNING: 'warning',
  CRITICAL: 'error',
  EMERGENCY: 'error'
};

function ZoneCard({ zone }) {
  return (
    <Card>
      <CardContent>
        <Typography variant="h6" component="div" gutterBottom>
          {zone.name}
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Status: {zone.status}
        </Typography>
        <LinearProgress variant="determinate" value={zone.densityPercentage} color={statusColors[zone.status]} sx={{ margin: '1rem 0' }} />
        <Typography variant="body2">
          {zone.currentOccupancy} / {zone.capacity} people ({zone.densityPercentage.toFixed(1)}%)
        </Typography>
      </CardContent>
    </Card>
  );
}

export default ZoneCard;
