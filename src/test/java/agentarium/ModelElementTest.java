package agentarium;

import agentarium.attributes.AttributeSetCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the {@link ModelElement} abstract class via a dummy subclass.
 */
public class ModelElementTest {

    private static class DummyModelElement extends ModelElement {
        private boolean wasRunCalled = false;

        public DummyModelElement(String name, AttributeSetCollection attributeSetCollection) {
            super(name, attributeSetCollection);
        }

        @Override
        public void run() {
            wasRunCalled = true;
        }

        public boolean wasRunCalled() {
            return wasRunCalled;
        }
    }

    private AttributeSetCollection attributeSetCollection;
    private DummyModelElement dummyElement;

    @BeforeEach
    public void setup() {
        attributeSetCollection = mock(AttributeSetCollection.class);
        dummyElement = new DummyModelElement("TestElement", attributeSetCollection);
    }

    @Test
    public void testNameIsStoredCorrectly() {
        assertEquals("TestElement", dummyElement.getName(), "Model element name should match the constructor input.");
    }

    @Test
    public void testAttributeSetCollectionIsReturned() {
        assertSame(attributeSetCollection, dummyElement.getAttributeSetCollection(),
                "Attribute set collection should be the same as passed in.");
    }

    @Test
    public void testSetupInitialisesAttributeCollection() {
        AttributeSetCollection mockAttributeSetCollection = mock(AttributeSetCollection.class);
        DummyModelElement dummy = new DummyModelElement("TestElement", mockAttributeSetCollection);

        dummy.setup();

        verify(mockAttributeSetCollection).setup("TestElement");
    }

    @Test
    public void testRunMethodCanBeInvoked() {
        dummyElement.run();
        assertTrue(dummyElement.wasRunCalled(), "Run method should change internal flag when invoked.");
    }

    @Test
    public void testModelElementAccessorCanBeSetAndRetrieved() {
        ModelElementAccessor mockAccessor = new ModelElementAccessor(
                dummyElement, null, null, null, null, null
        );
        dummyElement.setModelElementAccessor(mockAccessor);
        assertSame(mockAccessor, dummyElement.getModelElementAccessor(), "Accessor should be retrievable after being set.");
    }
}
