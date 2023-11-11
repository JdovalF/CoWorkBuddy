import React from 'react';
import { useDrop } from "react-dnd"
import DraggItemComponent from './DraggItemComponent';

const DropTargetContainerComponent = ({ data, onDrop, active }) => {

    const [{ isOver }, drop] = useDrop({
      accept: 'WORKER',
      drop: (worker) => onDrop(worker, data.taskId),
      collect: (monitor) => ({
        isOver: !!monitor.isOver(),
      }),
    });
  
    const workers = Array.isArray(data) ? data : data.workers;
    const cssStyle = {
      minHeight: "10rem",
      minWidth: '20rem',
      padding: '0.3rem',
      margin: '1rem 0.2rem',
      borderRadius: '1rem'
      }
  
    return (
      <div ref={drop} style={!active ? {...cssStyle, border: '2px dashed #dc3545'} : {...cssStyle, border: '2px dashed #0d6efd'}}>
        <div style={{ opacity: isOver ? 0.5 : 1 }}>
            {workers.map((worker, index) => (
              <DraggItemComponent key={`${worker.id}-${index}`} worker={worker} />
            ))}
        </div>
      </div>
    );
  };
  
  export default DropTargetContainerComponent;
