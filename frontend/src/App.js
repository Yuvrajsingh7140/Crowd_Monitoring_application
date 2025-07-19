
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Container from '@mui/material/Container';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import ZoneCard from './components/ZoneCard';
import Alert from '@mui/material/Alert';
import CircularProgress from '@mui/material/CircularProgress';

function App() {
  const [dashboard, setDashboard] = useState(null);
  const [error, setError] = useState(null);

  const fetchData = async () => {
    try {
      const res = await axios.get('/api/dashboard/overview');
      setDashboard(res.data);
    } catch (err) {
      setError('Error loading dashboard data');
    }
  };

  useEffect(() => {
    fetchData();
    const interval = setInterval(fetchData, 30000);
    return () => clearInterval(interval);
  }, []);

  if (error) {
    return <Alert severity="error">{error}</Alert>;
  }

  if (!dashboard) {
    return <Container sx={{ textAlign: 'center', marginTop: '4rem' }}>
      <CircularProgress />
      <Typography variant="h6" sx={{ marginTop: '1rem' }}>Loading Dashboard...</Typography>
    </Container>;
  }

  return (
    <Container sx={{ marginTop: '2rem' }}>
      <Typography variant="h3" gutterBottom>🚨 Crowd Monitoring Dashboard</Typography>

      <Typography variant="h6" sx={{ marginBottom: '2rem' }}>
        System Status: <strong>{dashboard.status}</strong>
      </Typography>

      <Grid container spacing={2}>
        {dashboard.zones.allZones.map(zone => (
          <Grid item xs={12} sm={6} md={4} key={zone.id}>
            <ZoneCard zone={zone} />
          </Grid>
        ))}
      </Grid>

      <Typography variant="caption" display="block" sx={{ marginTop: '2rem' }}>
        Last update: {new Date(dashboard.lastUpdate).toLocaleString()}
      </Typography>
    </Container>
  );
}

export default App;
